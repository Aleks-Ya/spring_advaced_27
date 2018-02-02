package booking.service;

import booking.domain.Auditorium;
import booking.domain.Event;
import booking.domain.Rate;
import booking.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

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

    public Auditorium createBlueHall() {
        return auditoriumService.create(new Auditorium("Blue hall", 15, Arrays.asList(1, 2, 3, 4, 5)));
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

    /**
     * Make the datasource empty.
     */
    public void cleanup() {
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
