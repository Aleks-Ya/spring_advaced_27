package beans.configuration;

import beans.daos.AuditoriumDAO;
import beans.daos.BookingDAO;
import beans.daos.EventDAO;
import beans.daos.UserDAO;
import beans.daos.mocks.*;
import beans.models.*;
import beans.services.*;
import beans.services.discount.BirthdayStrategy;
import beans.services.discount.DiscountStrategy;
import beans.services.discount.TicketsStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 13/2/16
 * Time: 3:36 PM
 */
@Configuration
public class TestBookingServiceConfiguration {

    @Bean
    public DiscountStrategy birthdayBookingStrategy() {
        return new BirthdayStrategy(0.15, 0);
    }

    @Bean
    public DiscountStrategy ticketsBookingStrategy() {
        return new TicketsStrategy(bookingBookingDAO(), 0.5, 3, 0);
    }

    @Bean
    public BookingDAO bookingBookingDAO() {
        HashSet<Ticket> tickets = new HashSet<Ticket>() {
            {
                addAll(tickets());
            }
        };
        return new BookingDAOBookingMock(new HashMap<User, Set<Ticket>>() {
            {
                put(testUser1(), tickets);
            }
        });
    }

    @Bean
    public DiscountService discountBookingServiceImpl() {
        return new DiscountServiceImpl(Arrays.asList(birthdayBookingStrategy(), ticketsBookingStrategy()));
    }

    @Bean
    public EventDAO eventDAOMock() {
        return new EventDAOMock(Arrays.asList(testEvent1(), testEvent2()));
    }

    @Bean
    public EventService eventServiceImpl() {
        return new EventServiceImpl(eventDAOMock());
    }

    @Bean
    public Event testEvent1() {
        return new Event(1, "Test event", beans.models.Rate.HIGH, 124.0, java.time.LocalDateTime.of(2016, 2, 6, 14, 45, 0),
                         testHall1());
    }

    @Bean
    public Event testEvent2() {
        return new Event(2, "Test event2", Rate.MID, 500.0, java.time.LocalDateTime.of(2016, 12, 6, 9, 35, 0),
                         testHall2());
    }

    @Bean
    public User testUser1() {
        return new User(0, "dmitriy.vbabichev@gmail.com", "Dmytro Babichev", java.time.LocalDate.of(1992, 4, 29));
    }

    @Bean
    public User testUser2() {
        return new User(1, "laory@yandex.ru", "Dmytro Babichev", java.time.LocalDate.of(1992, 4, 29));
    }

    @Bean
    public Ticket testTicket1() {
        return new Ticket(1, testEvent1(), java.time.LocalDateTime.of(2016, 2, 6, 14, 45, 0), Arrays.asList(3, 4),
                          testUser1(), 32D);
    }

    @Bean
    public Ticket testTicket2() {
        return new Ticket(2, testEvent2(), java.time.LocalDateTime.of(2016, 2, 7, 14, 45, 0), Arrays.asList(1, 2),
                          testUser1(), 123D);
    }

    @Bean
    public List<Ticket> tickets() {
        return Arrays.asList(testTicket1(), testTicket2());
    }

    @Bean
    public Auditorium testHall1() {
        return new Auditorium(1, "Test auditorium", 15, Arrays.asList(1, 2, 3, 4, 5));
    }

    @Bean
    public Auditorium testHall2() {
        return new Auditorium(2, "Test auditorium 2", 8, Collections.singletonList(1));
    }

    @Bean
    public AuditoriumDAO auditoriumDAO() {
        return new DBAuditoriumDAOMock(Arrays.asList(testHall1(), testHall2()));
    }

    @Bean
    public AuditoriumService auditoriumServiceImpl() {
        return new AuditoriumServiceImpl(auditoriumDAO());
    }


    @Bean
    public UserDAO userDAOMock() {
        return new UserDAOMock(Arrays.asList(testUser1()));
    }

    @Bean
    public UserService userServiceImpl() {
        return new UserServiceImpl(userDAOMock());
    }

    @Bean(name = "testBookingServiceImpl")
    public BookingService bookingServiceImpl() {
        return new BookingServiceImpl(eventServiceImpl(), auditoriumServiceImpl(), userServiceImpl(),
                                      discountBookingServiceImpl(), bookingBookingDAO(), 1, 2, 1.2, 1);
    }
}
