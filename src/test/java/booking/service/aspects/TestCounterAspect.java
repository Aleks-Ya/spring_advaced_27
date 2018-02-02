package booking.service.aspects;

import booking.BaseServiceTest;
import booking.domain.Event;
import booking.domain.Ticket;
import booking.domain.User;
import booking.service.aspects.mocks.CountAspectMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 13/2/16
 * Time: 7:20 PM
 */
//@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {CounterAspect.class})
//@Transactional
@Ignore("fix it")//TODO
public class TestCounterAspect extends BaseServiceTest {

    @Autowired
    private CounterAspect counterAspect;

    @Before
    public void initAspectMock() {
        CountAspectMock.cleanup();
//        auditoriumDAOMock.init();
//        userDAOMock.init();
//        eventDAOMock.init();
//        bookingDAOBookingMock.init();
    }

    @After
    public void cleanupAspectMock() {
        CountAspectMock.cleanup();
//        auditoriumDAOMock.cleanup();
//        userDAOMock.cleanup();
//        eventDAOMock.cleanup();
//        bookingDAOBookingMock.cleanup();
    }

    @Test
    public void testAccessedByName() {
        Event testEvent1 = testObjects.createParty();
        eventService.getByName("testName1");
        eventService.getByName("testName2");
        eventService.getByName("testName2");
        eventService.getByName("testName3");
        eventService.getByName(testEvent1.getName());
        eventService.getEvent(testEvent1.getName(), testEvent1.getAuditorium(), testEvent1.getDateTime());
        eventService.getEvent(testEvent1.getName(), testEvent1.getAuditorium(), testEvent1.getDateTime());
        eventService.getByName(testEvent1.getName());
        HashMap<String, Integer> expected = new HashMap<String, Integer>() {{
            put("testName1", 1);
            put("testName2", 2);
            put("testName3", 1);
            put(testEvent1.getName(), 4);
        }};
        assertEquals(expected, CounterAspect.getAccessByNameStat());
    }

    @Test
    public void testGetPriceByName() {
        Event event = testObjects.createParty();
        User user = testObjects.createJohn();
        List<Integer> seats = asList(1, 2, 3, 4);
        bookingService.getTicketPrice(event.getName(), event.getAuditorium().getName(), event.getDateTime(), seats,
                user);
        bookingService.getTicketPrice(event.getName(), event.getAuditorium().getName(), event.getDateTime(), seats,
                user);
        HashMap<String, Integer> expected = new HashMap<String, Integer>() {{
            put(event.getName(), 2);
        }};
        assertEquals(expected, CounterAspect.getGetPriceByNameStat());
    }

    @Test
    public void testBookTicketByName() {
        User user = testObjects.createJohn();
        Ticket ticket1 = testObjects.createTicketToParty();
        Ticket ticket2 = testObjects.createTicketToHackathon();
        bookingService.create(user.getId(),
                new Ticket(ticket1.getEvent(), ticket1.getDateTime(), asList(5, 6), user, ticket1.getPrice()));
        bookingService.create(user.getId(),
                new Ticket(ticket1.getEvent(), ticket1.getDateTime(), asList(7, 8), user, ticket1.getPrice()));
        bookingService.create(user.getId(),
                new Ticket(ticket2.getEvent(), ticket2.getDateTime(), asList(7, 8), user,
                        ticket2.getPrice()));
        HashMap<String, Integer> expected = new HashMap<String, Integer>() {{
            put(ticket1.getEvent().getName(), 2);
            put(ticket2.getEvent().getName(), 1);
        }};
//        assertEquals(expected, CounterAspect.getBookTicketByNameStat());
    }
}
