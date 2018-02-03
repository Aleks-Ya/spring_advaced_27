package booking.service.impl.discount;

import booking.BaseServiceTest;
import booking.domain.Event;
import booking.domain.Ticket;
import booking.domain.User;
import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 2:16 AM
 */
public class TicketsStrategyTest extends BaseServiceTest {

    private static final double TICKETS_DISCOUNT_VALUE = 0.5;
    private static final double DEFAULT_DISCOUNT = 0.01;
    private static final int DISCOUNT_THRESHOLD = 10;
    private TicketsStrategy strategy;

    @Before
    public void setUp() {
        strategy = new TicketsStrategy(bookingService,
                TICKETS_DISCOUNT_VALUE, DISCOUNT_THRESHOLD, DEFAULT_DISCOUNT);
    }

    @Test
    public void testCalculateDiscount_UserHasDiscount() {
        User user = testObjects.createJohn();
        Event event = testObjects.createParty();
        for (int i = 1; i < DISCOUNT_THRESHOLD; i++) {
            Ticket ticket = ticketService.create(new Ticket(event, event.getDateTime(), singletonList(i), event.getBasePrice()));
            bookingService.create(user.getId(), ticket);
        }
        double discount = strategy.calculateDiscount(user);
        assertThat(discount, equalTo(TICKETS_DISCOUNT_VALUE));
    }

    @Test
    public void testCalculateDiscount_UserHasNoDiscount() {
        User user = testObjects.createJohn();
        double discount = strategy.calculateDiscount(user);
        assertThat(discount, equalTo(DEFAULT_DISCOUNT));
    }
}
