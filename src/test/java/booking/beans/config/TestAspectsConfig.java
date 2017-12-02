package booking.beans.config;

import booking.beans.aspects.CounterAspect;
import booking.beans.aspects.DiscountAspect;
import booking.beans.aspects.LuckyWinnerAspect;
import booking.beans.aspects.mocks.DiscountAspectMock;
import booking.beans.aspects.mocks.LuckyWinnerAspectMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 13/2/16
 * Time: 7:18 PM
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class TestAspectsConfig extends TestBookingServiceConfig {

    @Bean
    public CounterAspect counterAspect() {
        return new CounterAspect();
    }

    @Bean
    DiscountAspect discountAspect() {
        return new DiscountAspectMock();
    }

    @Bean
    LuckyWinnerAspect luckyWinnerAspect() {
        return new LuckyWinnerAspectMock(99);
    }
}
