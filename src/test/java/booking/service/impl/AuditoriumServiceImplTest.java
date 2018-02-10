package booking.service.impl;

import booking.BaseServiceTest;
import booking.domain.Auditorium;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class AuditoriumServiceImplTest extends BaseServiceTest {

    @Test
    public void testDelete() {
        Auditorium auditorium = to.createBlueHall();
        long auditoriumId = auditorium.getId();
        assertThat(auditoriumService.getById(auditoriumId).get(), equalTo(auditorium));
        auditoriumService.delete(auditoriumId);
        assertFalse(auditoriumService.getById(auditoriumId).isPresent());
    }

    @Test
    public void testGetAuditoriums() {
        Auditorium auditorium1 = to.createBlueHall();
        Auditorium auditorium2 = to.createRedHall();
        List<Auditorium> auditoriums = auditoriumService.getAll();
        assertThat(auditoriums, containsInAnyOrder(auditorium1, auditorium2));
    }

    @Test
    public void testGetById() {
        Auditorium auditorium = to.createBlueHall();
        Auditorium actAuditorium = auditoriumService.getById(auditorium.getId())
                .orElseThrow(() -> new IllegalArgumentException("Auditorium not found by id: " + auditorium.getId()));//TODO add AuditoriumNotFoundException
        assertThat(actAuditorium, equalTo(auditorium));
    }
}
