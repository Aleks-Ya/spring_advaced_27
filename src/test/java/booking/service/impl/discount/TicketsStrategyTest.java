package booking.service.impl.discount;

import booking.BaseTest;
import booking.domain.Ticket;
import booking.domain.User;
import booking.repository.config.TestStrategiesConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 2:16 AM
 */
//@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestStrategiesConfig.class
//        , TicketDaoDiscountMock.class
})
@Transactional
public class TicketsStrategyTest extends BaseTest {


//    @Autowired
//    private TicketDaoDiscountMock bookingDAODiscountMock;

    @Ignore("fix it") //TODO
    @Test
    public void testCalculateDiscount_UserHasDiscount() throws Exception {
        Ticket ticketToParty = testObjects.createTicketToParty();
        TicketsStrategy strategy = new TicketsStrategy(ticketDao, 0.5, 2, 0);
        System.out.println(strategy.getClass());
        User userWithDiscount = new User("test@ema.il", ticketToParty.getUser().getEmail(),
                LocalDate.now(), null, null);
        double discount = strategy.calculateDiscount(userWithDiscount);
//        assertEquals("User: [" + userWithDiscount + "] has tickets discount", strategy.ticketsDiscountValue, discount, 0.00001);
    }

    @Test
    public void testCalculateDiscount_UserHasNoDiscount() throws Exception {
        TicketsStrategy strategy = new TicketsStrategy(ticketDao, 0.5, 2, 0);
        User userWithoutDiscount = new User("test@ema.il", "Test Name 2",
                LocalDate.now().minus(1, ChronoUnit.DAYS), null, null);
        double discount = strategy.calculateDiscount(userWithoutDiscount);
        assertEquals("User: [" + userWithoutDiscount + "] doesn't have tickets discount", strategy.defaultDiscount, discount, 0.00001);
    }
}
