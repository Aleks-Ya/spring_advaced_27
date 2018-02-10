package booking.service.aspects;

import booking.BaseServiceTest;
import booking.domain.Event;
import booking.domain.Ticket;
import booking.domain.User;
import booking.service.aspects.mocks.DiscountAspectMock;
import booking.service.impl.discount.BirthdayStrategy;
import booking.service.impl.discount.TicketsStrategy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = AspectConfig.class)
@TestPropertySource(properties = "tickets.discount.threshold=2")
public class DiscountAspectTest extends BaseServiceTest {

    @Before
    public void init() {
        DiscountAspectMock.cleanup();
    }

    @After
    public void cleanup() {
        DiscountAspectMock.cleanup();
    }

    @Test
    public void testCalculateDiscount() {
        User user = to.createJohnBornToday();
        Ticket ticket = to.createTicketToParty();
        Event event = ticket.getEvent();
        Long userId = user.getId();
        to.bookTicketToParty(userId, event.getId(), "5,6");
        to.bookTicketToParty(userId, event.getId(), "7,8");
        to.bookTicketToParty(userId, event.getId(), "9,10");
        List<Integer> seats = asList(1, 2, 3, 4);
        bookingService.getTicketPrice(event.getId(), seats, userId);
        bookingService.getTicketPrice(event.getId(), seats, userId);
        bookingService.getTicketPrice(event.getId(), seats, userId);
        bookingService.getTicketPrice(event.getId(), seats, userId);
        HashMap<String, Map<String, Integer>> expected = new HashMap<String, Map<String, Integer>>() {{
            put(TicketsStrategy.class.getSimpleName(), new HashMap<String, Integer>() {{
                put(user.getEmail(), 4);
            }});
            put(BirthdayStrategy.class.getSimpleName(), new HashMap<String, Integer>() {{
                put(user.getEmail(), 4);
            }});
        }};
        assertEquals(expected, DiscountAspect.getDiscountStatistics());
    }
}
