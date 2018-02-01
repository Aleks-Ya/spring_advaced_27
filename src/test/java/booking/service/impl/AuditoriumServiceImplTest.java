package booking.service.impl;

import booking.BaseTest;
import booking.domain.Auditorium;
import booking.repository.config.DataSourceConfig;
import booking.repository.config.DbSessionFactoryConfig;
import booking.repository.impl.AuditoriumDAOImpl;
import booking.service.AuditoriumService;
import booking.service.TestObjects;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
@ContextConfiguration(classes = {DataSourceConfig.class, DbSessionFactoryConfig.class, AuditoriumServiceImpl.class,
        AuditoriumDAOImpl.class, TestObjects.class
})
public class AuditoriumServiceImplTest extends BaseTest {

    @Autowired
    private AuditoriumService auditoriumService;
    @Autowired
    private TestObjects testObjects;

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
        List<Auditorium> auditoriums = auditoriumService.getAuditoriums();
        assertThat(auditoriums, containsInAnyOrder(auditorium1, auditorium2));
        testObjects.deleteAuditorium(auditorium1, auditorium2);
    }

    @Test
    public void testGetByName() {
        Auditorium auditorium1 = testObjects.createBlueHall();
        assertThat(auditoriumService.getByName(auditorium1.getName()), equalTo(auditorium1));
        testObjects.deleteAuditorium(auditorium1);
    }

    @Test
    public void testGetById() {
        Auditorium auditorium1 = testObjects.createBlueHall();
        assertThat(auditoriumService.getById(auditorium1.getId()), equalTo(auditorium1));
        testObjects.deleteAuditorium(auditorium1);
    }

    @Test(expected = RuntimeException.class)
    public void testGetByName_Exception() {
        auditoriumService.getSeatsNumber("bla-bla-bla");
    }
}
