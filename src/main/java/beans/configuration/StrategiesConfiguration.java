package beans.configuration;

import beans.services.discount.DiscountStrategy;
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
public class StrategiesConfiguration {

    @Autowired
    @Qualifier("birthdayStrategy")
    private DiscountStrategy birthdayStrategy;

    @Autowired
    @Qualifier("ticketsStrategy")
    private DiscountStrategy ticketsStrategy;

    @Bean(name = "strategies")
    public List<DiscountStrategy> strategies() {
        return Arrays.asList(birthdayStrategy, ticketsStrategy);
    }
}
