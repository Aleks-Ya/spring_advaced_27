package booking.service.impl.discount;

import booking.BaseTest;
import booking.domain.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

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
@ContextConfiguration(classes = BirthdayStrategy.class)
public class BirthdayStrategyTest extends BaseTest {

    @Autowired
    private BirthdayStrategy strategy;

    @Test
    public void testCalculateDiscount_UserHasDiscount() {
        User userWithDiscount = new User("a@bk.ru", "Test Name",
                LocalDate.now(), null, null);
        double discount = strategy.calculateDiscount(userWithDiscount);
        assertThat(discount, equalTo(0.05));
    }

    @Test
    public void testCalculateDiscount_UserHasNoDiscount() {
        User userWithoutDiscount = new User("a@bk.ru", "Test Name",
                LocalDate.now().minus(1, ChronoUnit.DAYS), null, null);
        double discount = strategy.calculateDiscount(userWithoutDiscount);
        assertThat(discount, equalTo(0D));
    }
}
