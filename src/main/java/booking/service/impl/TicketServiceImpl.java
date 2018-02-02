package booking.service.impl;

import booking.domain.*;
import booking.repository.TicketDao;
import booking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Aleksey Yablokov
 */
@Service
@PropertySource({"classpath:strategies/booking.properties"})
@Transactional
public class TicketServiceImpl implements TicketService {

    private final int minSeatNumber;
    private final double vipSeatPriceMultiplier;
    private final double highRatedPriceMultiplier;
    private final double defaultRateMultiplier;
    private final EventService eventService;
    private final AuditoriumService auditoriumService;
    private final UserService userService;
    private final TicketDao ticketDao;
    private final DiscountService discountService;

    @Autowired
    public TicketServiceImpl(@Qualifier("eventServiceImpl") EventService eventService,
                             @Qualifier("auditoriumServiceImpl") AuditoriumService auditoriumService,
                             @Qualifier("userServiceImpl") UserService userService,
                             @Qualifier("discountServiceImpl") DiscountService discountService,
                             TicketDao ticketDao,
                             @Value("${min.seat.number}") int minSeatNumber,
                             @Value("${vip.seat.price.multiplier}") double vipSeatPriceMultiplier,
                             @Value("${high.rate.price.multiplier}") double highRatedPriceMultiplier,
                             @Value("${def.rate.price.multiplier}") double defaultRateMultiplier) {
        this.eventService = eventService;
        this.auditoriumService = auditoriumService;
        this.userService = userService;
        this.ticketDao = ticketDao;
        this.discountService = discountService;
        this.minSeatNumber = minSeatNumber;
        this.vipSeatPriceMultiplier = vipSeatPriceMultiplier;
        this.highRatedPriceMultiplier = highRatedPriceMultiplier;
        this.defaultRateMultiplier = defaultRateMultiplier;
    }

    @Override
    public double getTicketPrice(String eventName, String auditoriumName, LocalDateTime dateTime, List<Integer> seats,
                                 User user) {
        if (Objects.isNull(eventName)) {
            throw new NullPointerException("Event name is [null]");
        }
        if (Objects.isNull(seats)) {
            throw new NullPointerException("Seats are [null]");
        }
        if (Objects.isNull(user)) {
            throw new NullPointerException("User is [null]");
        }
        if (seats.contains(null)) {
            throw new NullPointerException("Seats contain [null]");
        }

        final Auditorium auditorium = auditoriumService.getByName(auditoriumName);

        final Event event = eventService.getEvent(eventName, auditorium, dateTime);
        if (Objects.isNull(event)) {
            throw new IllegalStateException(
                    "There is no event with name: [" + eventName + "] in auditorium: [" + auditorium + "] on date: ["
                            + dateTime + "]");
        }

        final double baseSeatPrice = event.getBasePrice();
        final double rateMultiplier = event.getRate() == Rate.HIGH ? highRatedPriceMultiplier : defaultRateMultiplier;
        final double seatPrice = baseSeatPrice * rateMultiplier;
        final double vipSeatPrice = vipSeatPriceMultiplier * seatPrice;
        final double discount = discountService.getDiscount(user, event);


        validateSeats(seats, auditorium);

        final List<Integer> auditoriumVipSeats = auditorium.getVipSeatsList();
        final List<Integer> vipSeats = auditoriumVipSeats.stream().filter(seats::contains).collect(
                Collectors.toList());
        final List<Integer> simpleSeats = seats.stream().filter(seat -> !vipSeats.contains(seat)).collect(
                Collectors.toList());

        final double simpleSeatsPrice = simpleSeats.size() * seatPrice;
        final double vipSeatsPrice = vipSeats.size() * vipSeatPrice;
        final double totalPrice = simpleSeatsPrice + vipSeatsPrice;

        return (1.0 - discount) * totalPrice;
    }

    private void validateSeats(List<Integer> seats, Auditorium auditorium) {
        final int seatsNumber = auditorium.getSeatsNumber();
        final Optional<Integer> incorrectSeat = seats.stream().filter(
                seat -> seat < minSeatNumber || seat > seatsNumber).findFirst();
        incorrectSeat.ifPresent(seat -> {
            throw new IllegalArgumentException(
                    String.format("Seat: [%s] is incorrect. Auditorium: [%s] has [%s] seats", seat, auditorium.getName(),
                            seatsNumber));
        });
    }

    @Override
    public List<Ticket> getTicketsForEvent(String eventName, Long auditoriumId, LocalDateTime date) {
        final Auditorium auditorium = auditoriumService.getById(auditoriumId);
        final Event foundEvent = eventService.getEvent(eventName, auditorium, date);
        return ticketDao.getTickets(foundEvent);
    }

    @Override
    public List<Ticket> getBookedTickets() {
        return ticketDao.getBookedTickets();
    }

    @Override
    public List<Ticket> getAll() {
        return ticketDao.getAll();
    }

    @Override
    public Ticket getTicketById(Long ticketId) {
        return ticketDao.getTicketById(ticketId).orElse(null);
    }

    @Override
    public void delete(long ticketId) {
        ticketDao.delete(ticketId);
    }
}
