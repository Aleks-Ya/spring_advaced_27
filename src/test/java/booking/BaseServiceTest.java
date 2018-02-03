package booking;

import booking.repository.RepositoryConfig;
import booking.repository.TicketDao;
import booking.service.*;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

/**
 * @author Aleksey Yablokov
 */
@ContextConfiguration(classes = {RepositoryConfig.class, ServiceConfig.class, TestObjects.class})
@TestPropertySource("classpath:db-test.properties")
public abstract class BaseServiceTest extends BaseTest {
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