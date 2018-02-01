package booking.service;

import booking.domain.Auditorium;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

@Component
public class TestObjects {

    @Autowired
    private AuditoriumService auditoriumService;

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
}
