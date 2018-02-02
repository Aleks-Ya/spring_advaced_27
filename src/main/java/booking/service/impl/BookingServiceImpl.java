package booking.service.impl;

import booking.domain.Booking;
import booking.domain.Ticket;
import booking.domain.User;
import booking.repository.BookingDao;
import booking.repository.TicketDao;
import booking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/3/2016
 * Time: 11:33 AM
 */
@Service
@PropertySource({"classpath:strategies/booking.properties"})
@Transactional
public class BookingServiceImpl implements BookingService {

    private final int minSeatNumber;
    private final double vipSeatPriceMultiplier;
    private final double highRatedPriceMultiplier;
    private final double defaultRateMultiplier;
    private final EventService eventService;
    private final AuditoriumService auditoriumService;
    private final UserService userService;
    private final BookingDao bookingDao;
    private final TicketDao ticketDao;
    private final DiscountService discountService;
    private final TicketService ticketService;

    @Autowired
    public BookingServiceImpl(
            EventService eventService,
            @Qualifier("auditoriumServiceImpl") AuditoriumService auditoriumService,
            @Qualifier("userServiceImpl") UserService userService,
            @Qualifier("discountServiceImpl") DiscountService discountService,
            BookingDao bookingDao,
            TicketDao ticketDao,
            @Value("${min.seat.number}") int minSeatNumber,
            @Value("${vip.seat.price.multiplier}") double vipSeatPriceMultiplier,
            @Value("${high.rate.price.multiplier}") double highRatedPriceMultiplier,
            @Value("${def.rate.price.multiplier}") double defaultRateMultiplier,
            TicketService ticketService) {
        this.eventService = eventService;
        this.auditoriumService = auditoriumService;
        this.userService = userService;
        this.bookingDao = bookingDao;
        this.discountService = discountService;
        this.ticketDao = ticketDao;
        this.minSeatNumber = minSeatNumber;
        this.vipSeatPriceMultiplier = vipSeatPriceMultiplier;
        this.highRatedPriceMultiplier = highRatedPriceMultiplier;
        this.defaultRateMultiplier = defaultRateMultiplier;
        this.ticketService = ticketService;
    }

    @Override
    public Booking create(User user, Ticket ticket) {
        if (Objects.isNull(user)) {
            throw new NullPointerException("User is [null]");
        }
        User foundUser = userService.getById(user.getId());
        if (Objects.isNull(foundUser)) {
            throw new IllegalStateException("User: [" + user + "] is not registered");
        }

        List<Ticket> bookedTickets = ticketDao.getTickets(ticket.getEvent());
        boolean seatsAreAlreadyBooked = bookedTickets.stream()
                .anyMatch(bookedTicket -> ticket.getSeatsList().stream()
                        .anyMatch(bookedTicket.getSeatsList()::contains));

        if (!seatsAreAlreadyBooked)
            return bookingDao.create(user, ticket);
        else
            throw new IllegalStateException("Unable to book ticket: [" + ticket + "]. Seats are already booked.");
    }

    @Override
    public List<Booking> getAll() {
        return bookingDao.getAll();
    }

    @Override
    public void delete(long bookingId) {
        bookingDao.delete(bookingId);
    }
}
