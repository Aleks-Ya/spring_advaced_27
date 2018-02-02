package booking.repository.config;

import booking.service.aspects.CounterAspect;
import booking.service.aspects.DiscountAspect;
import booking.service.aspects.LuckyWinnerAspect;
import booking.service.aspects.mocks.DiscountAspectMock;
import booking.service.aspects.mocks.LuckyWinnerAspectMock;
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
@Deprecated
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
        return new LuckyWinnerAspectMock();
//        return new LuckyWinnerAspectMock(99);
    }
}
