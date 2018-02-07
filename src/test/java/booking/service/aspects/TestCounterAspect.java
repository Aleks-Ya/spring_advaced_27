package booking.service.aspects;

import booking.BaseServiceTest;
import booking.domain.Event;
import booking.domain.Ticket;
import booking.domain.User;
import booking.service.aspects.mocks.CountAspectMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@ContextConfiguration(classes = AspectConfig.class)
public class TestCounterAspect extends BaseServiceTest {

    @Before
    public void initAspectMock() {
        CountAspectMock.cleanup();
    }

    @After
    public void cleanupAspectMock() {
        CountAspectMock.cleanup();
    }

    @Test
    public void testAccessedByName() {
        Event testEvent1 = testObjects.createParty();
        eventService.getByName("testName1");
        eventService.getByName("testName2");
        eventService.getByName("testName2");
        eventService.getByName("testName3");
        eventService.getByName(testEvent1.getName());
        eventService.getEvent(testEvent1.getId(), testEvent1.getAuditorium(), testEvent1.getDateTime());
        eventService.getEvent(testEvent1.getId(), testEvent1.getAuditorium(), testEvent1.getDateTime());
        eventService.getByName(testEvent1.getName());
        HashMap<String, Integer> expected = new HashMap<String, Integer>() {{
            put("testName1", 1);
            put("testName2", 2);
            put("testName3", 1);
            put(testEvent1.getName(), 2);
        }};
        assertThat(CounterAspect.getAccessByNameStat(), equalTo(expected));
    }

    @Test
    public void testGetPriceByName() {
        Event event = testObjects.createParty();
        User user = testObjects.createJohn();
        List<Integer> seats = asList(1, 2, 3, 4);
        bookingService.getTicketPrice(event.getId(), event.getAuditorium().getName(), event.getDateTime(), seats, user);
        bookingService.getTicketPrice(event.getId(), event.getAuditorium().getName(), event.getDateTime(), seats, user);
        assertThat(CounterAspect.getGetPriceByNameStat(), anEmptyMap());
    }

    @Test
    public void testBookTicketByName() {
        User user = testObjects.createJohnWithAccount();
        Ticket party = testObjects.createTicketToParty();
        Ticket hackathon = testObjects.createTicketToHackathon();

        Ticket ticket1 = ticketService.create(new Ticket(party.getEvent(), party.getDateTime(), asList(5, 6), party.getPrice()));
        bookingService.bookTicket(user.getId(), ticket1);

        Ticket ticket2 = ticketService.create(new Ticket(party.getEvent(), party.getDateTime(), asList(7, 8), party.getPrice()));
        bookingService.bookTicket(user.getId(), ticket2);

        Ticket ticket3 = ticketService.create(new Ticket(hackathon.getEvent(), hackathon.getDateTime(), asList(7, 8), hackathon.getPrice()));
        bookingService.bookTicket(user.getId(), ticket3);

        HashMap<String, Integer> expected = new HashMap<String, Integer>() {{
            put(party.getEvent().getName(), 2);
            put(hackathon.getEvent().getName(), 1);
        }};
        assertThat(CounterAspect.getBookTicketByNameStat(), equalTo(expected));
    }
}
