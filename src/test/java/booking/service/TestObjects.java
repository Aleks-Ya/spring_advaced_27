package booking.service;

import booking.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static java.util.Arrays.asList;

/**
 * Provides convenient methods for creating and deleting domain objects in tests.
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
    private BookingService bookingService;

    public Auditorium createBlueHall() {
        return auditoriumService.create(new Auditorium("Blue hall", 15, asList(1, 2, 3, 4, 5)));
    }

    public Auditorium createRedHall() {
        return auditoriumService.create(new Auditorium("Red hall", 8, Collections.singletonList(1)));
    }

    public User createJohn() {
        return userService.register(new User("john@gmail.com", "John Smith",
                LocalDate.of(1980, 3, 20), "jpass", null));
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
        User user = createJohn();
        Event event = createParty();
        Ticket ticket = new Ticket(event, event.getDateTime(), asList(5, 6), user, event.getBasePrice() * 2);
        return bookingService.bookTicket(user, ticket);
    }

    /**
     * Make the datasource empty.
     */
    public void cleanup() {
        for (Ticket ticket : bookingService.getBookedTickets()) {
            bookingService
        }
        for (Event event : eventService.getAll()) {
            eventService.delete(event);
        }
        for (Auditorium auditorium : auditoriumService.getAuditoriums()) {
            auditoriumService.delete(auditorium.getId());
        }
        for (User user : userService.getAll()) {
            userService.delete(user);
        }
    }
}
