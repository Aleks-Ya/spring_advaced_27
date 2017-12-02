package booking.beans.config;

import booking.beans.services.DiscountStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 13/2/16
 * Time: 3:36 PM
 */
@Configuration
public class StrategiesConfig {
    private final DiscountStrategy birthdayStrategy;
    private final DiscountStrategy ticketsStrategy;

    @Autowired
    public StrategiesConfig(@Qualifier("birthdayStrategy") DiscountStrategy birthdayStrategy,
                            @Qualifier("ticketsStrategy") DiscountStrategy ticketsStrategy) {
        this.birthdayStrategy = birthdayStrategy;
        this.ticketsStrategy = ticketsStrategy;
    }

    @Bean(name = "strategies")
    public List<DiscountStrategy> strategies() {
        return Arrays.asList(birthdayStrategy, ticketsStrategy);
    }
}
