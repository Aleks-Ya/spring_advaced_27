package booking.service.aspects;

import booking.BaseServiceTest;
import booking.domain.Ticket;
import booking.domain.User;
import booking.service.aspects.mocks.LuckyWinnerAspectMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = AspectConfig.class)
@TestPropertySource(properties = {"lucky.enabled=true", "lucky.percentage=100"})
public class LuckyWinnerAspectTest extends BaseServiceTest {

    @Before
    public void initAspect() {
        LuckyWinnerAspectMock.cleanup();
    }

    @After
    public void cleanupAspect() {
        LuckyWinnerAspectMock.cleanup();
    }

    @Test
    public void testCalculateDiscount() {
        User user = testObjects.createJohnBornToday();
        Ticket ticket1 = testObjects.createTicketToParty();
        bookingService.create(user.getId(),
                new Ticket(ticket1.getEvent(), ticket1.getDateTime(), asList(5, 6), ticket1.getPrice()));
        bookingService.create(user.getId(),
                new Ticket(ticket1.getEvent(), ticket1.getDateTime(), asList(7, 8), ticket1.getPrice()));
        bookingService.create(user.getId(),
                new Ticket(ticket1.getEvent(), ticket1.getDateTime(), asList(9, 10), ticket1.getPrice()));
        bookingService.create(user.getId(),
                new Ticket(ticket1.getEvent(), ticket1.getDateTime(), asList(11, 12), ticket1.getPrice()));

        assertEquals(Collections.singletonList(user.getEmail()), LuckyWinnerAspectMock.getLuckyUsers());
    }
}
