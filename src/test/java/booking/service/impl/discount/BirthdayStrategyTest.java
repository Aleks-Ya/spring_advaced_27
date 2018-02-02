package booking.service.impl.discount;

import booking.BaseTest;
import booking.domain.User;
import booking.repository.config.TestStrategiesConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 2:16 AM
 */
@ContextConfiguration(classes = TestStrategiesConfig.class)
public class BirthdayStrategyTest extends BaseTest {

    @Autowired
    private BirthdayStrategy strategy;

    @Test
    public void testCalculateDiscount_UserHasDiscount() {
        User userWithDiscount = new User("test@ema.il", "Test Name", LocalDate.now(), null, null);
        double discount = strategy.calculateDiscount(userWithDiscount);
        assertEquals("User: [" + userWithDiscount + "] has birthday discount", strategy.birthdayDiscountValue, discount, 0.00001);
    }

    @Test
    public void testCalculateDiscount_UserHasNoDiscount() {
        User userWithoutDiscount = new User("test@ema.il", "Test Name", LocalDate.now().minus(1, ChronoUnit.DAYS), null, null);
        double discount = strategy.calculateDiscount(userWithoutDiscount);
        assertEquals("User: [" + userWithoutDiscount + "] doesn't have birthday discount", strategy.defaultDiscountValue, discount, 0.00001);
    }
}
