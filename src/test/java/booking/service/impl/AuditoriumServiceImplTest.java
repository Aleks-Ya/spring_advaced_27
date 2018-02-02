package booking.service.impl;

import booking.BaseTest;
import booking.domain.Auditorium;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 1:23 PM
 */
public class AuditoriumServiceImplTest extends BaseTest {

    @Test
    public void testDelete() {
        Auditorium auditorium = testObjects.createBlueHall();
        long auditoriumId = auditorium.getId();
        assertThat(auditoriumService.getById(auditoriumId), equalTo(auditorium));
        auditoriumService.delete(auditoriumId);
        assertNull(auditoriumService.getById(auditoriumId));
    }

    @Test
    public void testGetAuditoriums() {
        Auditorium auditorium1 = testObjects.createBlueHall();
        Auditorium auditorium2 = testObjects.createRedHall();
        List<Auditorium> auditoriums = auditoriumService.getAll();
        assertThat(auditoriums, containsInAnyOrder(auditorium1, auditorium2));
    }

    @Test
    public void testGetByName() {
        Auditorium auditorium1 = testObjects.createBlueHall();
        assertThat(auditoriumService.getByName(auditorium1.getName()), equalTo(auditorium1));
    }

    @Test
    public void testGetById() {
        Auditorium auditorium1 = testObjects.createBlueHall();
        assertThat(auditoriumService.getById(auditorium1.getId()), equalTo(auditorium1));
    }

    @Test(expected = RuntimeException.class)
    public void testGetByName_Exception() {
        auditoriumService.getSeatsNumber("bla-bla-bla");
    }
}
