package booking.service.impl;

import booking.domain.Auditorium;
import booking.domain.Booking;
import booking.domain.Event;
import booking.domain.Rate;
import booking.domain.Ticket;
import booking.domain.User;
import booking.repository.BookingDao;
import booking.service.AuditoriumService;
import booking.service.BookingService;
import booking.service.DiscountService;
import booking.service.EventService;
import booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@PropertySource({"classpath:strategies/booking.properties"})
public class BookingServiceImpl implements BookingService {

    private final int minSeatNumber;
    private final double vipSeatPriceMultiplier;
    private final double highRatedPriceMultiplier;
    private final double defaultRateMultiplier;
    private final EventService eventService;
    private final AuditoriumService auditoriumService;
    private final UserService userService;
    private final BookingDao bookingDao;
    private final DiscountService discountService;

    @Autowired
    public BookingServiceImpl(
            EventService eventService,
            AuditoriumService auditoriumService,
            UserService userService,
            DiscountService discountService,
            BookingDao bookingDao,
            @Value("${min.seat.number}") int minSeatNumber,
            @Value("${vip.seat.price.multiplier}") double vipSeatPriceMultiplier,
            @Value("${high.rate.price.multiplier}") double highRatedPriceMultiplier,
            @Value("${def.rate.price.multiplier}") double defaultRateMultiplier) {
        this.eventService = eventService;
        this.auditoriumService = auditoriumService;
        this.userService = userService;
        this.bookingDao = bookingDao;
        this.discountService = discountService;
        this.minSeatNumber = minSeatNumber;
        this.vipSeatPriceMultiplier = vipSeatPriceMultiplier;
        this.highRatedPriceMultiplier = highRatedPriceMultiplier;
        this.defaultRateMultiplier = defaultRateMultiplier;
    }

    @Override
    public Booking bookTicket(long userId, Ticket ticket) {
        User foundUser = userService.getById(userId);
        if (Objects.isNull(foundUser)) {
            throw new IllegalStateException("User: [" + userId + "] is not registered");
        }

        List<Ticket> bookedTickets = bookingDao.getTicketsForEvent(ticket.getEvent().getId());
        boolean seatsAreAlreadyBooked = bookedTickets.stream()
                .anyMatch(bookedTicket -> ticket.getSeatsList().stream()
                        .anyMatch(bookedTicket.getSeatsList()::contains));

        if (!seatsAreAlreadyBooked) {
            return bookingDao.create(userId, ticket);
        } else {
            throw new IllegalStateException("Unable to book ticket: [" + ticket + "]. Seats are already booked.");
        }
    }

    @Override
    public Booking getById(long bookingId) {
        return bookingDao.getById(bookingId);
    }

    @Override
    public long countTickets(long userId) {
        return bookingDao.countTickets(userId);
    }

    @Override
    public List<Booking> getAll() {
        return bookingDao.getAll();
    }

    @Override
    public void delete(long bookingId) {
        bookingDao.delete(bookingId);
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
    public List<Ticket> getBookedTickets() {
        return bookingDao.getBookedTickets();
    }

    @Override
    public List<Ticket> getTicketsForEvent(long eventId) {
        return bookingDao.getTicketsForEvent(eventId);
    }

}
