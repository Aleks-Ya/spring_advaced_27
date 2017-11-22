package booking.web.controller;

import booking.beans.configuration.TestBookingServiceConfiguration;
import booking.beans.configuration.db.DataSourceConfiguration;
import booking.beans.configuration.db.DbSessionFactory;
import booking.beans.daos.BookingDAO;
import booking.beans.daos.mocks.BookingDAOBookingMock;
import booking.beans.daos.mocks.DBAuditoriumDAOMock;
import booking.beans.daos.mocks.EventDAOMock;
import booking.beans.daos.mocks.UserDAOMock;
import booking.beans.models.Auditorium;
import booking.beans.models.Event;
import booking.beans.models.Rate;
import booking.beans.models.Ticket;
import booking.beans.models.User;
import booking.beans.services.AuditoriumService;
import booking.beans.services.BookingService;
import booking.beans.services.EventService;
import booking.beans.services.UserService;
import booking.web.configuration.FreeMarkerConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {FreeMarkerConfig.class, BookingController.class, DbSessionFactory.class,
        DataSourceConfiguration.class, TestBookingServiceConfiguration.class
})
@Transactional
public class BookingControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private BookingDAOBookingMock bookingDAOBookingMock;
    @Autowired
    private EventDAOMock eventDAOMock;
    @Autowired
    private UserDAOMock userDAOMock;
    @Autowired
    private DBAuditoriumDAOMock auditoriumDAOMock;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingDAO bookingDAO;
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private AuditoriumService auditoriumService;

    private MockMvc mvc;

    @Before
    public void setup() {
        auditoriumDAOMock.init();
        userDAOMock.init();
        eventDAOMock.init();
        bookingDAOBookingMock.init();
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void getBookedTickets() throws Exception {
        deleteAllTickets();
        Ticket ticket1 = createTicket();
        Ticket ticket2 = createTicket();
        mvc.perform(get(BookingController.ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().string(format("<h1>Booked tickets</h1>\n" +
                                "<p>Ticket</p>\n" +
                                "<p>Id: %s</p>\n" +
                                "<p>Event: %s</p>\n" +
                                "<p>Date: 2017-01-15T10:30</p>\n" +
                                "<p>Seats: 1,2,3</p>\n" +
                                "<p>User: Mat</p>\n" +
                                "<p>Price: 100</p><hr/>\n" +
                                "<p>Ticket</p>\n" +
                                "<p>Id: %s</p>\n" +
                                "<p>Event: %s</p>\n" +
                                "<p>Date: 2017-01-15T10:30</p>\n" +
                                "<p>Seats: 1,2,3</p>\n" +
                                "<p>User: Mat</p>\n" +
                                "<p>Price: 100</p><hr/>\n",
                        ticket1.getId(), ticket1.getEvent().getName(),
                        ticket2.getId(), ticket2.getEvent().getName())));
    }

    private void deleteAllTickets() {
        bookingDAO.getAllTickets().forEach(ticket -> bookingDAO.delete(ticket.getUser(), ticket));
    }

    @Test
    public void getTicketById() throws Exception {
        Ticket bookedTicket = createTicket();
        mvc.perform(get(BookingController.ENDPOINT + "/id/" + bookedTicket.getId())
        )
                .andExpect(status().isOk())
                .andExpect(content().string(format("<h1>Ticket</h1>\n" +
                                "<p>Id: %s</p>\n" +
                                "<p>Event: %s</p>\n" +
                                "<p>Date: 2017-01-15T10:30</p>\n" +
                                "<p>Seats: 1,2,3</p>\n" +
                                "<p>User: Mat</p>\n" +
                                "<p>Price: 100</p>",
                        bookedTicket.getId(), bookedTicket.getEvent().getName())));
    }

    @Test
    public void bookTicket() throws Exception {
        deleteAllTickets();
        User user = createUser();
        Event event = createEvent();
        mvc.perform(post(BookingController.ENDPOINT)
                .param("userId", String.valueOf(user.getId()))
                .param("eventId", String.valueOf(event.getId()))
                .param("localDateTime", "2007-12-03T10:15:30")
                .param("seats", "1,2,3")
                .param("price", "100.5")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(format("<h1>The ticket is booked</h1>\n" +
                                "<p>Id: 3</p>\n" +
                                "<p>Event: %s</p>\n" +
                                "<p>Date: 2007-12-03T10:15:30</p>\n" +
                                "<p>Seats: 1,2,3</p>\n" +
                                "<p>User: Mat</p>\n" +
                                "<p>Price: 100.5</p>",
                        event.getName())));
    }

    @Test
    public void getTicketPrice() throws Exception {
        deleteAllTickets();
        Auditorium auditorium = auditoriumService.create(new Auditorium("Room", 100, Arrays.asList(1, 2, 3)));
        User user = createUser();
        LocalDateTime date = LocalDateTime.of(2018, 1, 15, 10, 30);
        Event event = eventService.create(new Event(UUID.randomUUID() + "Meeting", Rate.HIGH, 100,
                date, auditorium));
        mvc.perform(get(BookingController.ENDPOINT + "/price")
                .param("eventName", event.getName())
                .param("auditoriumName", auditorium.getName())
                .param("userId", String.valueOf(user.getId()))
                .param("localDateTime", date.toString())
                .param("seats", "1,2,3")
        )
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>Ticket price</h1>\n" +
                        "720\n"));
    }

    @Test
    public void getTicketsForEvent() throws Exception {
        deleteAllTickets();
        Auditorium auditorium = auditoriumService.create(new Auditorium("Room", 100, Arrays.asList(1, 2, 3)));
        LocalDateTime date = LocalDateTime.of(2018, 1, 15, 10, 30);
        User user = createUser();
        Event event = eventService.create(new Event(UUID.randomUUID() + "Meeting", Rate.HIGH, 100,
                date, auditorium));
        Ticket ticket = bookingService.bookTicket(user, new Ticket(event, date, Arrays.asList(1, 2, 3), user, 100));
        mvc.perform(get(BookingController.ENDPOINT + "/tickets")
                .param("eventName", event.getName())
                .param("auditoriumId", String.valueOf(auditorium.getId()))
                .param("localDateTime", date.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().string(format("<h1>Ticket for event</h1>\n" +
                        "<p>Events:\n" +
                        "    %s;\n" +
                        "</p>\n" +
                        "<p>Ticket</p>\n" +
                        "<p>Id: %s</p>\n" +
                        "<p>Event: %s</p>\n" +
                        "<p>Date: 2018-01-15T10:30</p>\n" +
                        "<p>Seats: 1,2,3</p>\n" +
                        "<p>User: Mat</p>\n" +
                        "<p>Price: 100</p><hr/>\n",
                        event.getName(),
                        ticket.getId(),
                        event.getName()
                )));
    }

    private Ticket createTicket() {
        User user = createUser();
        Event event = createEvent();
        Ticket ticket = new Ticket(event, LocalDateTime.of(2017, 1, 15, 10, 30),
                Arrays.asList(1, 2, 3), user, 100);
        return bookingService.bookTicket(user, ticket);
    }

    private Event createEvent() {
        return eventService.create(new Event(UUID.randomUUID() + "Meeting", Rate.HIGH, 100, null, null));
    }

    private User createUser() {
        User userObj = new User(UUID.randomUUID() + "mat@gmail.com", "Mat", null);
        return userService.register(userObj);
    }
}