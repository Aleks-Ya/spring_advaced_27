package booking.service.impl;

import booking.domain.Account;
import booking.domain.Auditorium;
import booking.domain.Booking;
import booking.domain.Event;
import booking.domain.Rate;
import booking.domain.Ticket;
import booking.domain.User;
import booking.repository.BookingDao;
import booking.service.AccountService;
import booking.service.BookingService;
import booking.service.DiscountService;
import booking.service.EventService;
import booking.service.TicketService;
import booking.service.UserService;
import booking.web.controller.SeatHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@PropertySource({"classpath:strategies/booking.properties"})
public class BookingServiceImpl implements BookingService {

    private final int minSeatNumber;
    private final double vipSeatPriceMultiplier;
    private final double highRatedPriceMultiplier;
    private final double defaultRateMultiplier;
    private final EventService eventService;
    private final UserService userService;
    private final BookingDao bookingDao;
    private final DiscountService discountService;
    private final AccountService accountService;
    private final TicketService ticketService;

    @Autowired
    public BookingServiceImpl(
            EventService eventService,
            UserService userService,
            DiscountService discountService,
            BookingDao bookingDao,
            AccountService accountService,
            @Value("${min.seat.number}") int minSeatNumber,
            @Value("${vip.seat.price.multiplier}") double vipSeatPriceMultiplier,
            @Value("${high.rate.price.multiplier}") double highRatedPriceMultiplier,
            @Value("${def.rate.price.multiplier}") double defaultRateMultiplier,
            TicketService ticketService) {
        this.eventService = eventService;
        this.userService = userService;
        this.bookingDao = bookingDao;
        this.discountService = discountService;
        this.minSeatNumber = minSeatNumber;
        this.vipSeatPriceMultiplier = vipSeatPriceMultiplier;
        this.highRatedPriceMultiplier = highRatedPriceMultiplier;
        this.defaultRateMultiplier = defaultRateMultiplier;
        this.accountService = accountService;
        this.ticketService = ticketService;
    }

    @Override
    @Transactional
    public Booking bookTicket(long userId, long eventId, String seats, Double price) {
        User user = userService.getById(userId);
        Event event = eventService.getById(eventId);
        List<Integer> seatsList = SeatHelper.parseSeatsString(seats);
        Double priceValue = price != null ? price : event.getBasePrice();
        Ticket ticket = ticketService.create(new Ticket(event, seatsList, priceValue));

        if (Objects.isNull(user)) {
            throw new IllegalStateException("User: [" + userId + "] is not registered");
        }

        List<Ticket> bookedTickets = bookingDao.getTicketsForEvent(ticket.getEvent().getId());
        boolean seatsAreAlreadyBooked = bookedTickets.stream()
                .anyMatch(bookedTicket -> ticket.getSeatsList().stream()
                        .anyMatch(bookedTicket.getSeatsList()::contains));

        if (seatsAreAlreadyBooked) {
            throw new IllegalStateException("Unable to book ticket: [" + ticket + "]. Seats are already booked.");
        }

        Account account = accountService.getByUserId(userId);
        if (account == null) {
            account = accountService.create(new Account(user, BigDecimal.ZERO));
        }

        BigDecimal priceDec = BigDecimal.valueOf(ticket.getPrice());
        BigDecimal seatCount = BigDecimal.valueOf(ticket.getSeatsList().size());
        BigDecimal requiredAmount = priceDec.multiply(seatCount);

        //TODO move to AccountService#withdrawal
        BigDecimal availableAmount = account.getAmount();
        if (availableAmount.compareTo(requiredAmount) < 0) {
            throw new IllegalStateException("Not enough money to buy ticket " + ticket + ". Available amount " + availableAmount);
        }

        accountService.withdrawal(account, requiredAmount);

        return bookingDao.create(userId, ticket);
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
    public double getTicketPrice(long eventId, List<Integer> seats, User user) {
        if (Objects.isNull(seats)) {
            throw new NullPointerException("Seats are [null]");
        }
        if (Objects.isNull(user)) {
            throw new NullPointerException("User is [null]");
        }
        if (seats.contains(null)) {
            throw new NullPointerException("Seats contain [null]");
        }


        final Event event = eventService.getById(eventId);
        if (Objects.isNull(event)) {
            throw new IllegalStateException("There is no event with id: " + eventId);
        }
        final Auditorium auditorium = event.getAuditorium();

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
    public List<Ticket> getTicketsForEvent(long eventId) {
        return bookingDao.getTicketsForEvent(eventId);
    }

}
