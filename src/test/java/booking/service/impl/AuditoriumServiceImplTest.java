package booking.service.impl;

import booking.domain.Auditorium;
import booking.repository.config.DataSourceConfig;
import booking.repository.config.DbSessionFactoryConfig;
import booking.repository.config.PropertySourceConfig;
import booking.repository.config.TestAuditoriumConfig;
import booking.repository.mocks.DBAuditoriumDAOMock;
import booking.service.AuditoriumService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 1:23 PM
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PropertySourceConfig.class, DataSourceConfig.class, DbSessionFactoryConfig.class,
        TestAuditoriumConfig.class})
@Transactional
public class AuditoriumServiceImplTest {

    private static final int AUDITORIUMS_COUNT = 2;
    @Autowired
    private AuditoriumService auditoriumService;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private DBAuditoriumDAOMock auditoriumDAOMock;

    private Auditorium testHall1;
    private Auditorium testHall2;

    @Before
    public void init() {
        auditoriumDAOMock.init();
        testHall1 = (Auditorium) applicationContext.getBean("testHall1");
        testHall2 = (Auditorium) applicationContext.getBean("testHall2");
    }

    @After
    public void cleanup() {
        auditoriumDAOMock.cleanup();
    }

    @Test
    public void testGetAuditoriums() {
        List<Auditorium> auditoriums = auditoriumService.getAuditoriums();
        assertEquals("Auditoriums number should match", AUDITORIUMS_COUNT, auditoriums.size());
    }

    @Test
    public void testGetByName() {
        checkTestHall(testHall1);
        checkTestHall(testHall2);
    }

    @Test
    public void testGetById() {
        assertNotNull(auditoriumService.getById(testHall1.getId()));
    }

    @Test(expected = RuntimeException.class)
    public void testGetByName_Exception() {
        auditoriumService.getSeatsNumber("bla-bla-bla");
    }

    private void checkTestHall(Auditorium testHall) {
        int seatsNumber = auditoriumService.getSeatsNumber(testHall.getName());
        List<Integer> vipSeats = auditoriumService.getVipSeats(testHall.getName());
        assertEquals("Auditorium seats number should match. Auditorium: [" + testHall + "]", testHall.getSeatsNumber(),
                seatsNumber);
        assertEquals("Auditorium vip seats should match. Auditorium: [" + testHall + "]", testHall.getVipSeatsList(),
                vipSeats);
    }
}
