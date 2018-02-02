package booking;

import booking.repository.TicketDao;
import booking.repository.config.DataSourceConfig;
import booking.repository.config.DbSessionFactoryConfig;
import booking.repository.impl.*;
import booking.service.*;
import booking.service.impl.*;
import booking.service.impl.discount.DiscountConfig;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Aleksey Yablokov
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BookingServiceImpl.class,
        DataSourceConfig.class, DbSessionFactoryConfig.class, AuditoriumServiceImpl.class,
        AuditoriumDAOImpl.class, TestObjects.class, UserServiceImpl.class, UserDAOImpl.class,
        EventServiceImpl.class, EventDAOImpl.class, DiscountConfig.class,
        BookingServiceImpl.class, TicketDaoImpl.class, TicketServiceImpl.class, BookingDaoImpl.class
})
@TestPropertySource("classpath:db-test.properties")
public abstract class BaseTest {
    @Autowired
    protected TestObjects testObjects;

    @Autowired
    protected TicketDao ticketDao;

    @Autowired
    protected TicketService ticketService;

    @Autowired
    protected BookingService bookingService;

    @Autowired
    protected AuditoriumService auditoriumService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected EventService eventService;

    @After
    public void clean() {
        testObjects.cleanup();
    }
}