package booking.service;

import booking.domain.*;
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
import java.util.List;

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

    private int userCounter = 0;

    public User createJohn() {
//        userCounter++;
//        return userService.register(new User(
//                format("john_%d@gmail.com", userCounter),
//                "John Smith " + userCounter,
//                LocalDate.of(1980, 3, 20), "jpass", Roles.REGISTERED_USER));
        return userBuilder().amount(0).build();
    }

    public User createJohnWithAccount() {
//        return createAccount().getUser();
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
     * Create an user has both {@link Roles#REGISTERED_USER} and {@link Roles#BOOKING_MANAGER}.
     */
    public User createBookingManager() {
//        userCounter++;
//        return userService.register(new User(
//                format("john_%d@gmail.com", userCounter),
//                "John Smith " + userCounter,
//                LocalDate.of(1980, 3, 20), "jpass",
//                Roles.REGISTERED_USER + UserDaoUserDetailsService.ROLES_DELIMITER + Roles.BOOKING_MANAGER));
        return userBuilder().hasBookingManagerRole().build();
    }

    /**
     * Create an user that born today (for testing @{@link booking.service.impl.discount.BirthdayStrategy}.
     */
    public User createJohnBornToday() {
//        userCounter++;
//        return userService.register(new User(
//                format("john_%d@gmail.com", userCounter),
//                "John Smith " + userCounter,
//                LocalDate.now(), "jpass", Roles.REGISTERED_USER));
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
        Ticket ticket = new Ticket(event, event.getDateTime(), asList(100, 101), event.getBasePrice() * 2);
        return ticketService.create(ticket);
    }

    public Ticket createTicketToParty(Integer... seats) {
        Event event = createParty();
        Ticket ticket = new Ticket(event, event.getDateTime(), asList(seats), event.getBasePrice() * 2);
        return ticketService.create(ticket);
    }

    public Ticket createTicketToHackathon() {
        Event event = createHackathon();
        Ticket ticket = new Ticket(event, event.getDateTime(), asList(200, 201, 202), event.getBasePrice() * 3);
        return ticketService.create(ticket);
    }

    public Booking bookTicketToParty() {
        Event event = createParty();
        Ticket ticket = new Ticket(event, event.getDateTime(), asList(100, 101), event.getBasePrice() * 2);
        User user = createJohnWithAccount();
        return bookingService.bookTicket(user.getId(), ticket);
    }

    public Booking bookTicketToHackathon() {
        Event event = createHackathon();
        Ticket ticket = new Ticket(event, event.getDateTime(), asList(100, 101), event.getBasePrice() * 2);
        User user = createJohnWithAccount();
        return bookingService.bookTicket(user.getId(), ticket);
    }

    public Account createAccount() {
        User user = createJohn();
        Account account = new Account(user, BigDecimal.valueOf(10000));
        return accountService.create(account);
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

    public UserBuilder userBuilder() {
        return new UserBuilder(userService, accountService);
    }

    public static class UserBuilder {
        private static int userCounter = 0;
        private static final String BOOKING_MANAGER_ROLE = Roles.REGISTERED_USER + UserDaoUserDetailsService.ROLES_DELIMITER + Roles.BOOKING_MANAGER;

        private final UserService userService;
        private final AccountService accountService;

        private boolean birthdayToday = false;
        private boolean hasBookingManagerRole = false;
        private double amount = 10_000;

        UserBuilder(UserService userService, AccountService accountService) {
            this.userService = userService;
            this.accountService = accountService;
        }

        public UserBuilder birthdayToday() {
            birthdayToday = true;
            return this;
        }

        public UserBuilder hasBookingManagerRole() {
            hasBookingManagerRole = true;
            return this;
        }

        public UserBuilder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public User build() {
            userCounter++;

            LocalDate birthday = birthdayToday ? LocalDate.now() : LocalDate.of(1980, 3, 20);
            String roles = hasBookingManagerRole ? BOOKING_MANAGER_ROLE : Roles.REGISTERED_USER;


            User user = userService.register(new User(
                    format("john_%d@gmail.com", userCounter),
                    "John Smith " + userCounter,
                    birthday, "jpass", roles));

            if (amount > 0) {
                accountService.create(new Account(user, BigDecimal.valueOf(amount)));
            }

            return user;
        }
    }
}
