package booking.service;

import booking.domain.*;
import booking.web.security.Roles;
import booking.web.security.UserDaoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * Factory for convenient creating domain objects in tests.
 *
 * @author Aleksey Yablokov
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

    public Auditorium createBlueHall() {
        return auditoriumService.create(new Auditorium("Blue hall", 1000, asList(1, 2, 3, 4, 5)));
    }

    public Auditorium createRedHall() {
        return auditoriumService.create(new Auditorium("Red hall", 500, singletonList(1)));
    }

    private int userConuter = 0;

    public User createJohn() {
        userConuter++;
        return userService.register(new User(
                format("john_%d@gmail.com", userConuter),
                "John Smith " + userConuter,
                LocalDate.of(1980, 3, 20), "jpass", Roles.REGISTERED_USER));
    }

    /**
     * Create an user has both {@link Roles#REGISTERED_USER} and {@link Roles#BOOKING_MANAGER}.
     */
    public User createBookingManager() {
        userConuter++;
        return userService.register(new User(
                format("john_%d@gmail.com", userConuter),
                "John Smith " + userConuter,
                LocalDate.of(1980, 3, 20), "jpass",
                Roles.REGISTERED_USER + UserDaoUserDetailsService.ROLES_DELIMITER + Roles.BOOKING_MANAGER));
    }

    /**
     * Create an user that born today (for testing @{@link booking.service.impl.discount.BirthdayStrategy}.
     */
    public User createJohnBornToday() {
        userConuter++;
        return userService.register(new User(
                format("john_%d@gmail.com", userConuter),
                "John Smith " + userConuter,
                LocalDate.now(), "jpass", Roles.REGISTERED_USER));
    }

    public Event createParty() {
        Auditorium auditorium = createBlueHall();
        return eventService.create(new Event("New Year Party", Rate.HIGH, 5000,
                LocalDateTime.of(2018, 12, 31, 23, 0), auditorium));
    }

    public Event createHackathon() {
        Auditorium auditorium = createRedHall();
        return eventService.create(new Event("Java Hackathon", Rate.MID, 2000,
                LocalDateTime.of(2018, 3, 13, 9, 0), auditorium));
    }

    public Ticket createTicketToParty() {
        Event event = createParty();
        Ticket ticket = new Ticket(event, event.getDateTime(), asList(100, 101), event.getBasePrice() * 2);
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
        User user = createJohn();
        return bookingService.create(user.getId(), ticket);
    }

    public Booking bookTicketToHackathon() {
        Event event = createHackathon();
        Ticket ticket = new Ticket(event, event.getDateTime(), asList(100, 101), event.getBasePrice() * 2);
        User user = createJohn();
        return bookingService.create(user.getId(), ticket);
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
        for (User user : userService.getAll()) {
            userService.delete(user);
        }
    }
}
