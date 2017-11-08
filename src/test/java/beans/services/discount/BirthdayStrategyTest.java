package beans.services.discount;

import beans.models.User;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 2:16 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = beans.configuration.TestStrategiesConfiguration.class)
public class BirthdayStrategyTest {

    @Autowired
    private BirthdayStrategy strategy;

    @org.junit.Test
    public void testCalculateDiscount_UserHasDiscount() throws Exception {
        User userWithDiscount = new User("test@ema.il", "Test Name", LocalDate.now());
        double discount = strategy.calculateDiscount(userWithDiscount);
        assertEquals("User: [" + userWithDiscount + "] has birthday discount", strategy.birthdayDiscountValue, discount, 0.00001);
    }

    @org.junit.Test
    public void testCalculateDiscount_UserHasNoDiscount() throws Exception {
        User userWithoutDiscount = new User("test@ema.il", "Test Name", LocalDate.now().minus(1, ChronoUnit.DAYS));
        double discount = strategy.calculateDiscount(userWithoutDiscount);
        assertEquals("User: [" + userWithoutDiscount + "] doesn't have birthday discount", strategy.defaultDiscountValue, discount, 0.00001);
    }
}
