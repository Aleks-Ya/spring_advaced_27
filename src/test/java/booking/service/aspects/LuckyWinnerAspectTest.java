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

import java.time.LocalDate;
import java.util.Collections;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 13/2/16
 * Time: 7:20 PM
 */
@ContextConfiguration(classes = AspectConfig.class)
@TestPropertySource(properties = "lucky.percentage=100")
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
        User user = testObjects.createJohn();
        User discountUser = new User(user.getId(), user.getEmail(), user.getName(), LocalDate.now(), null, null);
        Ticket ticket1 = testObjects.createTicketToParty();
        bookingService.create(discountUser.getId(),
                new Ticket(ticket1.getEvent(), ticket1.getDateTime(), asList(5, 6), user, ticket1.getPrice()));
        bookingService.create(discountUser.getId(),
                new Ticket(ticket1.getEvent(), ticket1.getDateTime(), asList(7, 8), user, ticket1.getPrice()));
        bookingService.create(discountUser.getId(),
                new Ticket(ticket1.getEvent(), ticket1.getDateTime(), asList(9, 10), user, ticket1.getPrice()));
        bookingService.create(discountUser.getId(),
                new Ticket(ticket1.getEvent(), ticket1.getDateTime(), asList(11, 12), user, ticket1.getPrice()));

        assertEquals(Collections.singletonList(user.getEmail()), LuckyWinnerAspectMock.getLuckyUsers());
    }
}
