package booking.service;

import booking.domain.Account;
import booking.domain.Auditorium;
import booking.domain.Booking;
import booking.domain.Event;
import booking.domain.Rate;
import booking.domain.Ticket;
import booking.domain.User;
import booking.web.security.ExtendedUserDetails;
import booking.web.security.Roles;
import booking.web.security.UserDaoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * Factory for convenient creating domain objects in tests.
 */
@Component
public class TestObjects {

    @Autowired
    private AuditoriumService auditoriumService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private AccountService accountService;

    public Auditorium createBlueHall() {
        return auditoriumService.create(new Auditorium("Blue hall", 1000, asList(1, 2, 3, 4, 5)));
    }

    public Auditorium createRedHall() {
        return auditoriumService.create(new Auditorium("Red hall", 500, singletonList(1)));
    }

    public User createJohn() {
        return userBuilder().amount(0).build();
    }

    public User createJohnWithAccount() {
        return userBuilder().build();
    }

    /**
     * Create a new user and put him to SecurityContext, so {@link UserService#getCurrentUser()} returns him.
     */
    public User createCurrentUser() {
        User user = userBuilder().hasBookingManagerRole().build();
        List<SimpleGrantedAuthority> authorities = UserDaoUserDetailsService.rolesStrToAuthorities(user.getRoles());
        ExtendedUserDetails userDetails = new ExtendedUserDetails(user.getEmail(), user.getPassword(),
                authorities, user.getEmail(), user.getName());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return user;
    }

    /**
     * Create an user has {@link Roles#BOOKING_MANAGER} role.
     */
    public User createBookingManager() {
        return userBuilder().hasBookingManagerRole().build();
    }

    /**
     * Create an user that born today (for testing @{@link booking.service.impl.discount.BirthdayStrategy}.
     */
    public User createJohnBornToday() {
        return userBuilder().birthdayToday().build();
    }

    public Event createParty() {
        Auditorium auditorium = createBlueHall();
        return eventService.create(new Event("New Year Party", Rate.HIGH, 200,
                LocalDateTime.of(2018, 12, 31, 23, 0), auditorium));
    }

    public Event createHackathon() {
        Auditorium auditorium = createRedHall();
        return eventService.create(new Event("Java Hackathon", Rate.MID, 100,
                LocalDateTime.of(2018, 3, 13, 9, 0), auditorium));
    }

    public Ticket createTicketToParty() {
        Event event = createParty();
        Ticket ticket = new Ticket(event, asList(100, 101), event.getBasePrice() * 2);
        return ticketService.create(ticket);
    }

    public Booking bookTicketToParty() {
        User user = createJohnWithAccount();
        return bookTicketToParty(user.getId());
    }

    public Booking bookTicketToParty(long userId) {
        return bookTicketToParty(userId, "100,101");
    }

    public Booking bookTicketToParty(long userId, String seats) {
        Event event = createParty();
        return bookTicketToParty(userId, event.getId(), seats);
    }

    public Booking bookTicketToParty(long userId, long eventId, String seats) {
        Event event = eventService.getById(eventId);
        return bookingService.bookTicket(userId, eventId, seats, event.getBasePrice() * 2);
    }

    public Booking bookTicketToHackathon() {
        Event event = createHackathon();
        User user = createJohnWithAccount();
        return bookingService.bookTicket(user.getId(), event.getId(), "100,101", event.getBasePrice() * 2);
    }

    public Account createAccount() {
        User user = userBuilder().amount(10000).build();
        return accountService.getByUserId(user.getId());
    }

    public String getRawPassword(long userId) {
        return UserBuilder.getRawPassword(userId);
    }

    /**
     * Make the datasource empty.
     */
    public void cleanup() {
        for (Booking booking : bookingService.getAll()) {
            bookingService.delete(booking.getId());
        }
        for (Ticket ticket : ticketService.getAll()) {
            ticketService.delete(ticket.getId());
        }
        for (Event event : eventService.getAll()) {
            eventService.delete(event);
        }
        for (Auditorium auditorium : auditoriumService.getAll()) {
            auditoriumService.delete(auditorium.getId());
        }
        for (Account account : accountService.getAll()) {
            accountService.delete(account.getId());
        }
        for (User user : userService.getAll()) {
            userService.delete(user);
        }
    }

    private UserBuilder userBuilder() {
        return new UserBuilder(userService, accountService);
    }

    static class UserBuilder {
        private static final Map<Long, String> rawPasswords = new HashMap<>();
        private static int userCounter = 0;

        private final UserService userService;
        private final AccountService accountService;

        private boolean birthdayToday = false;
        private boolean hasBookingManagerRole = false;
        private double amount = 10_000;

        static String getRawPassword(long userId) {
            return rawPasswords.get(userId);
        }

        UserBuilder(UserService userService, AccountService accountService) {
            this.userService = userService;
            this.accountService = accountService;
        }

        UserBuilder birthdayToday() {
            birthdayToday = true;
            return this;
        }

        UserBuilder hasBookingManagerRole() {
            hasBookingManagerRole = true;
            return this;
        }

        UserBuilder amount(double amount) {
            this.amount = amount;
            return this;
        }

        User build() {
            userCounter++;

            LocalDate birthday = birthdayToday ? LocalDate.now() : LocalDate.of(1980, 3, 20);
            String roles = hasBookingManagerRole ? Roles.BOOKING_MANAGER : Roles.REGISTERED_USER;

            String rawPassword = "jpass";
            User user = userService.register(new User(
                    format("john_%d@gmail.com", userCounter),
                    "John Smith " + userCounter,
                    birthday, rawPassword, roles));

            if (amount > 0) {
                Account account = accountService.getByUserId(user.getId());
                accountService.refill(account, BigDecimal.valueOf(amount));
            }

            rawPasswords.put(user.getId(), rawPassword);

            return user;
        }
    }
}
