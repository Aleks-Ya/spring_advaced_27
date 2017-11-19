package booking.web.controller;

import booking.beans.configuration.db.DataSourceConfiguration;
import booking.beans.configuration.db.DbSessionFactory;
import booking.beans.daos.mocks.BookingDAOBookingMock;
import booking.beans.daos.mocks.DBAuditoriumDAOMock;
import booking.beans.daos.mocks.EventDAOMock;
import booking.beans.daos.mocks.UserDAOMock;
import booking.web.FreeMarkerConfig;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {FreeMarkerConfig.class, BookingController.class, DbSessionFactory.class, DataSourceConfiguration.class,
        booking.beans.configuration.TestBookingServiceConfiguration.class})
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
        mvc.perform(get(BookingController.ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>Booked tickets</h1>\n" +
                        "<p>Ticket</p>\n" +
                        "<p>Id: 1</p>\n" +
                        "<p>Event: Test event2</p>\n" +
                        "<p>Date: 2016-02-07T14:45</p>\n" +
                        "<p>Seats: 1,2</p>\n" +
                        "<p>User: Dmytro Babichev</p>\n" +
                        "<p>Price: 123</p><hr/>\n" +
                        "<p>Ticket</p>\n" +
                        "<p>Id: 2</p>\n" +
                        "<p>Event: Test event</p>\n" +
                        "<p>Date: 2016-02-06T14:45</p>\n" +
                        "<p>Seats: 3,4</p>\n" +
                        "<p>User: Dmytro Babichev</p>\n" +
                        "<p>Price: 32</p><hr/>\n"));
    }

}