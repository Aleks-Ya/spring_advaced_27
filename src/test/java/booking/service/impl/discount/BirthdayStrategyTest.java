package booking.service.impl.discount;

import booking.BaseTest;
import booking.domain.User;
import org.junit.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 2:16 AM
 */
public class BirthdayStrategyTest extends BaseTest {

    private static final double BIRTHDAY_DISCOUNT_VALUE = 0.1;
    private static final double DEFAULT_DISCOUNT_VALUE = 0.05;
    private static final BirthdayStrategy STRATEGY = new BirthdayStrategy(BIRTHDAY_DISCOUNT_VALUE, DEFAULT_DISCOUNT_VALUE);

    @Test
    public void testCalculateDiscount_UserHasDiscount() {
        User userWithDiscount = new User("a@bk.ru", "Test Name",
                LocalDate.now(), null, null);
        double discount = STRATEGY.calculateDiscount(userWithDiscount);
        assertThat(discount, equalTo(BIRTHDAY_DISCOUNT_VALUE));
    }

    @Test
    public void testCalculateDiscount_UserHasNoDiscount() {
        User userWithoutDiscount = new User("a@bk.ru", "Test Name",
                LocalDate.now().minus(1, ChronoUnit.DAYS), null, null);
        double discount = STRATEGY.calculateDiscount(userWithoutDiscount);
        assertThat(discount, equalTo(DEFAULT_DISCOUNT_VALUE));
    }
}
