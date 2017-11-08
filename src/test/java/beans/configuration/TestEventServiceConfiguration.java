package beans.configuration;

import beans.daos.EventDAO;
import beans.daos.mocks.EventDAOMock;
import beans.models.Event;
import beans.models.Rate;
import beans.services.EventService;
import beans.services.EventServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 13/2/16
 * Time: 6:10 PM
 */
@Configuration
public class TestEventServiceConfiguration extends TestAuditoriumConfiguration {

    @Bean
    @Scope("prototype")
    public Event testEvent1() {
        return new Event("Test event", beans.models.Rate.HIGH, 124.0, java.time.LocalDateTime.of(2016, 2, 6, 14, 45, 0),
                         testHall1());
    }

    @Bean
    @Scope("prototype")
    public Event testEvent2() {
        return new Event("Test event2", Rate.MID, 500.0, java.time.LocalDateTime.of(2016, 12, 6, 9, 35, 0),
                         testHall2());
    }

    @Bean
    @Scope("prototype")
    public Event testEvent3() {
        return new Event("Test event", Rate.LOW, 50.0, java.time.LocalDateTime.of(2016, 12, 29, 10, 0, 0),
                         testHall1());
    }

    @Bean(name = "testEventDAOMock")
    @Scope("prototype")
    public EventDAO eventDAOMock() {
        return new EventDAOMock(Arrays.asList(testEvent1(), testEvent2(), testEvent3()));
    }

    @Bean(name = "testEventServiceImpl")
    public EventService eventServiceImpl() {
        return new EventServiceImpl(eventDAOMock());
    }
}
