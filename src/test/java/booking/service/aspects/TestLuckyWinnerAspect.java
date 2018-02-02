package booking.service.aspects;

import booking.BaseServiceTest;
import booking.domain.Ticket;
import booking.domain.User;
import booking.service.aspects.mocks.LuckyWinnerAspectMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 13/2/16
 * Time: 7:20 PM
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        LuckyWinnerAspectMock.class
//        PropertySourceConfig.class, DataSourceConfig.class,
//        DbSessionFactoryConfig.class
//        , TestAspectsConfig.class
})
//@Transactional
//@TestPropertySource("classpath:aspects/aspects.properties")
@Ignore("fix it") //TODO
public class TestLuckyWinnerAspect extends BaseServiceTest {

//    @Autowired
//    private ApplicationContext applicationContext;
//
//    @Autowired
//    private BookingService bookingService;

//    @Autowired
//    private BookingDAOBookingMock bookingDAOBookingMock;
//
//    @Autowired
//    private EventDAOMock eventDAOMock;
//
//    @Autowired
//    private UserDAOMock userDAOMock;
//
    @Autowired
    private LuckyWinnerAspectMock luckyWinnerAspectMock;
//
//    @Autowired
//    private DBAuditoriumDAOMock auditoriumDAOMock;

    @Before
    public void initAspect() {
        LuckyWinnerAspectMock.cleanup();
//        auditoriumDAOMock.init();
//        userDAOMock.init();
//        eventDAOMock.init();
//        bookingDAOBookingMock.init();
    }

    @After
    public void cleanupAspect() {
        LuckyWinnerAspectMock.cleanup();
//        auditoriumDAOMock.cleanup();
//        userDAOMock.cleanup();
//        eventDAOMock.cleanup();
//        bookingDAOBookingMock.cleanup();
    }

    @Test
    public void testCalculateDiscount() {
        User user = testObjects.createJohn();
        User discountUser = new User(user.getId(), user.getEmail(), user.getName(), LocalDate.now(), null, null);
        Ticket ticket1 = testObjects.createTicketToParty();
        bookingService.create(discountUser,
                new Ticket(ticket1.getEvent(), ticket1.getDateTime(), Arrays.asList(5, 6), user, ticket1.getPrice()));
        bookingService.create(discountUser,
                new Ticket(ticket1.getEvent(), ticket1.getDateTime(), Arrays.asList(7, 8), user, ticket1.getPrice()));
        bookingService.create(discountUser,
                new Ticket(ticket1.getEvent(), ticket1.getDateTime(), Arrays.asList(9, 10), user, ticket1.getPrice()));
        bookingService.create(discountUser,
                new Ticket(ticket1.getEvent(), ticket1.getDateTime(), Arrays.asList(11, 12), user, ticket1.getPrice()));

        assertEquals(Collections.singletonList(user.getEmail()), LuckyWinnerAspectMock.getLuckyUsers());
    }
}
