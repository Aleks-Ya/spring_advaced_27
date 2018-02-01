package booking.service.impl;

import booking.BaseTest;
import booking.domain.Auditorium;
import booking.repository.config.DataSourceConfig;
import booking.repository.config.DbSessionFactoryConfig;
import booking.repository.impl.AuditoriumDAOImpl;
import booking.service.AuditoriumService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 1:23 PM
 */
@Transactional
@ContextConfiguration(classes = {DataSourceConfig.class, DbSessionFactoryConfig.class, AuditoriumServiceImpl.class,
        AuditoriumDAOImpl.class
})
public class AuditoriumServiceImplTest extends BaseTest {

    @Autowired
    private AuditoriumService auditoriumService;

    private Auditorium auditorium1;
    private Auditorium auditorium2;

    @Before
    public void init() {
        auditorium1 = auditoriumService.create(
                new Auditorium("auditorium 1", 15, Arrays.asList(1, 2, 3, 4, 5)));
        auditorium2 = auditoriumService.create(
                new Auditorium("auditorium 2", 8, Collections.singletonList(1)));
    }

    @After
    public void tearDown() {
        auditoriumService.delete(auditorium1.getId());
        auditoriumService.delete(auditorium2.getId());
    }

    @Test
    public void testGetAuditoriums() {
        List<Auditorium> auditoriums = auditoriumService.getAuditoriums();
        assertThat(auditoriums, containsInAnyOrder(auditorium1, auditorium2));
    }

    @Test
    public void testGetByName() {
        assertThat(auditoriumService.getByName(auditorium1.getName()), equalTo(auditorium1));
    }

    @Test
    public void testGetById() {
        assertThat(auditoriumService.getById(auditorium1.getId()), equalTo(auditorium1));
    }

    @Test(expected = RuntimeException.class)
    public void testGetByName_Exception() {
        auditoriumService.getSeatsNumber("bla-bla-bla");
    }
}
