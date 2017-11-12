package booking.beans.aspects;

import booking.beans.aspects.mocks.DiscountAspectMock;
import booking.beans.configuration.AppConfiguration;
import booking.beans.configuration.db.DataSourceConfiguration;
import booking.beans.configuration.db.DbSessionFactory;
import booking.beans.daos.mocks.BookingDAOBookingMock;
import booking.beans.daos.mocks.DBAuditoriumDAOMock;
import booking.beans.daos.mocks.EventDAOMock;
import booking.beans.daos.mocks.UserDAOMock;
import booking.beans.models.Event;
import booking.beans.models.Ticket;
import booking.beans.models.User;
import booking.beans.services.BookingService;
import booking.beans.services.EventService;
import booking.beans.services.discount.BirthdayStrategy;
import booking.beans.services.discount.TicketsStrategy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 13/2/16
 * Time: 7:20 PM
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfiguration.class, DataSourceConfiguration.class, DbSessionFactory.class,
                                 booking.beans.configuration.TestAspectsConfiguration.class})
@Transactional
public class TestDiscountAspect {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private EventService eventService;

    @Autowired
    private BookingDAOBookingMock bookingDAOBookingMock;

    @Autowired
    private EventDAOMock eventDAOMock;

    @Autowired
    private UserDAOMock userDAOMock;

    @Autowired
    private DiscountAspectMock discountAspect;

    @Autowired
    private DBAuditoriumDAOMock auditoriumDAOMock;

    @Before
    public void init() {
        DiscountAspectMock.cleanup();
        auditoriumDAOMock.init();
        userDAOMock.init();
        eventDAOMock.init();
        bookingDAOBookingMock.init();
    }

    @After
    public void cleanup() {
        DiscountAspectMock.cleanup();
        auditoriumDAOMock.cleanup();
        userDAOMock.cleanup();
        eventDAOMock.cleanup();
        bookingDAOBookingMock.cleanup();
    }

    @Test
    public void testCalculateDiscount() {
        Event event = (Event) applicationContext.getBean("testEvent1");
        User user = (User) applicationContext.getBean("testUser1");
        User discountUser = new User(user.getId(), user.getEmail(), user.getName(), LocalDate.now());
        Ticket ticket1 = (Ticket) applicationContext.getBean("testTicket1");
        bookingService.bookTicket(discountUser,
                                  new Ticket(ticket1.getEvent(), ticket1.getDateTime(), Arrays.asList(5, 6), user, ticket1.getPrice()));
        bookingService.bookTicket(discountUser,
                                  new Ticket(ticket1.getEvent(), ticket1.getDateTime(), Arrays.asList(7, 8), user, ticket1.getPrice()));
        bookingService.bookTicket(discountUser,
                                  new Ticket(ticket1.getEvent(), ticket1.getDateTime(), Arrays.asList(9, 10), user, ticket1.getPrice()));
        List<Integer> seats = Arrays.asList(1, 2, 3, 4);
        bookingService.getTicketPrice(event.getName(), event.getAuditorium().getName(), event.getDateTime(), seats, discountUser);
        bookingService.getTicketPrice(event.getName(), event.getAuditorium().getName(), event.getDateTime(), seats, discountUser);
        bookingService.getTicketPrice(event.getName(), event.getAuditorium().getName(), event.getDateTime(), seats, discountUser);
        bookingService.getTicketPrice(event.getName(), event.getAuditorium().getName(), event.getDateTime(), seats, discountUser);
        HashMap<String, Map<String, Integer>> expected = new HashMap<String, Map<String, Integer>>() {{
            put(TicketsStrategy.class.getSimpleName(), new HashMap<String, Integer>() {{
                put(user.getEmail(), 4);
            }});
            put(BirthdayStrategy.class.getSimpleName(), new HashMap<String, Integer>() {{
                put(user.getEmail(), 4);
            }});
        }};
        assertEquals(expected, DiscountAspect.getDiscountStatistics());
    }
}
