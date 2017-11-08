package beans.services.discount;

import beans.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/4/2016
 * Time: 11:25 AM
 */
@Component("birthdayStrategy")
@PropertySource({"classpath:strategies/strategies.properties"})
public class BirthdayStrategy implements DiscountStrategy {

    public final double birthdayDiscountValue;
    public final double defaultDiscountValue;

    @Autowired
    public BirthdayStrategy(@Value("${birthday.discount}") double birthdayDiscountValue,
                            @Value("${birthday.discount.default}") double defaultDiscountValue) {
        this.birthdayDiscountValue = birthdayDiscountValue;
        this.defaultDiscountValue = defaultDiscountValue;
    }

    @Override
    public double calculateDiscount(User user) {
        final LocalDate now = LocalDate.now();
        if (Objects.nonNull(user.getBirthday()) && user.getBirthday().getMonthValue() == now.getMonthValue() &&
            user.getBirthday().getDayOfMonth() == now.getDayOfMonth()) {
            return birthdayDiscountValue;
        }
        return defaultDiscountValue;
    }
}
