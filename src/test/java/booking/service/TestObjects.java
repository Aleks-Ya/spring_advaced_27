package booking.service;

import booking.domain.Auditorium;
import booking.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

    public Auditorium createBlueHall() {
        return auditoriumService.create(new Auditorium("Blue hall", 15, Arrays.asList(1, 2, 3, 4, 5)));
    }

    public Auditorium createRedHall() {
        return auditoriumService.create(new Auditorium("Red hall", 8, Collections.singletonList(1)));
    }

    public void deleteAuditorium(long auditoriumId) {
        auditoriumService.delete(auditoriumId);
    }

    public void deleteAuditorium(Auditorium... auditoriums) {
        for (Auditorium auditorium : auditoriums) {
            deleteAuditorium(auditorium.getId());
        }
    }

    public User createJohn() {
        return userService.register(new User("john@gmail.com", "John Smith",
                LocalDate.of(1980, 3, 20), "jpass", null));
    }

    public void deleteUser(User... users) {
        for (User user : users) {
            userService.delete(user);
        }
    }
}
