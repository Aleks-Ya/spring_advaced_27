package booking.web.security.booking;

import booking.BaseWebSecurityTest;
import booking.domain.Auditorium;
import booking.domain.Booking;
import booking.domain.Event;
import booking.domain.Ticket;
import booking.domain.User;
import booking.web.controller.BookingController;
import booking.web.controller.LoginController;
import booking.web.security.Roles;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import static booking.web.controller.RootControllerTest.NAVIGATOR;
import static java.lang.String.format;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Check that an user (has REGISTERED_USER role) has access to all operations (exclude "get booked tickets for event").
 */
@ContextConfiguration(classes = {BookingController.class, LoginController.class})
public class BookingRegisteredUserTest extends BaseWebSecurityTest {

    @Test
    public void getBookedTickets() throws Exception {
        User user = to.createBookingManager();
        MockHttpSession session = authenticateSession(user);
        mvc.perform(get(BookingController.SHOW_ALL_TICKETS_ENDPOINT).session(session))
                .andExpect(status().isOk());
    }

    @Test
    public void getTicketById() throws Exception {
        User user = to.createJohn();
        MockHttpSession session = authenticateSession(user);
        Ticket ticket = to.createTicketToParty();
        mvc.perform(get(BookingController.ROOT_ENDPOINT + "/id/" + ticket.getId()).session(session))
                .andExpect(status().isOk());
    }

    @Test
    public void bookTicket() throws Exception {
        User user = to.createJohn();
        MockHttpSession session = authenticateSession(user);
        Event event = to.createParty();
        mvc.perform(post(BookingController.ROOT_ENDPOINT).session(session)
                .param("userId", String.valueOf(user.getId()))
                .param("eventId", String.valueOf(event.getId()))
                .param("seats", "1,2,3")
                .param("price", "100.5")
        )
                .andExpect(status().isOk());
    }

    @Test
    public void getTicketPrice() throws Exception {
        User user = to.createJohnWithAccount();

        MockHttpSession session = authenticateSession(user);

        Booking booking = to.bookTicketToParty();
        Event event = booking.getTicket().getEvent();
        Auditorium auditorium = event.getAuditorium();

        mvc.perform(get(BookingController.PRICE_ENDPOINT).session(session)
                .param("eventName", event.getName())
                .param("auditoriumName", auditorium.getName())
                .param("userId", String.valueOf(user.getId()))
                .param("seats", "1,2,3")
        )
                .andExpect(status().isOk());
    }

    /**
     * User that possesses only {@link Roles#REGISTERED_USER} has no access to this endpoint
     * and must be redirected to The Access Denied page.
     */
    @Test
    public void getTicketsForEvent() throws Exception {
        User user = to.createJohnWithAccount();
        MockHttpSession session = authenticateSession(user);

        Booking booking = to.bookTicketToParty();
        Event event = booking.getTicket().getEvent();

        MvcResult mvcResult = mvc.perform(get(BookingController.SHOW_TICKETS_BY_EVENT_ENDPOINT)
                .session(session)
                .param("eventId", String.valueOf(event.getId())))
                .andExpect(status().isForbidden())
                .andReturn();

        String forwardedUrl = mvcResult.getResponse().getForwardedUrl();
        assertThat(forwardedUrl, endsWith(LoginController.ACCESS_DENIED_ENDPOINT));

        mvc.perform(get(forwardedUrl).session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(format(
                        "<p>%s (%s, $10,000, REGISTERED_USER, <a href='/logout'>logout</a>)</p>\n" +
                                NAVIGATOR +
                                "<h1>Access denied</h1>\n" +
                        "You have no permissions to visit this page.\n",
                        user.getName(),
                        user.getEmail()
                )));
    }
}