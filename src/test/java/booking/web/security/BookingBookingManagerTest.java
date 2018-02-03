package booking.web.security;

import booking.BaseWebSecurityTest;
import booking.domain.*;
import booking.web.controller.BookingController;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@ContextConfiguration(classes = BookingController.class)
public class BookingBookingManagerTest extends BaseWebSecurityTest {
    @Test
    public void getBookedTickets() throws Exception {
        User user = testObjects.createBookingManager();
        MockHttpSession session = authenticateSession(user);
        mvc.perform(get(BookingController.ENDPOINT).session(session))
                .andExpect(status().isOk());
    }

    @Test
    public void getTicketById() throws Exception {
        User user = testObjects.createBookingManager();
        MockHttpSession session = authenticateSession(user);
        Ticket ticket = testObjects.createTicketToParty();
        mvc.perform(get(BookingController.ENDPOINT + "/id/" + ticket.getId()).session(session))
                .andExpect(status().isOk());
    }

    @Test
    public void bookTicket() throws Exception {
        User user = testObjects.createBookingManager();
        MockHttpSession session = authenticateSession(user);
        Event event = testObjects.createParty();
        mvc.perform(post(BookingController.ENDPOINT).session(session)
                .param("userId", String.valueOf(user.getId()))
                .param("eventId", String.valueOf(event.getId()))
                .param("localDateTime", "2007-12-03T10:15:30")
                .param("seats", "1,2,3")
                .param("price", "100.5")
        )
                .andExpect(status().isCreated());
    }

    @Test
    public void getTicketPrice() throws Exception {
        User user = testObjects.createBookingManager();

        MockHttpSession session = authenticateSession(user);

        Booking booking = testObjects.bookTicketToParty();
        Event event = booking.getTicket().getEvent();
        Auditorium auditorium = event.getAuditorium();
        LocalDateTime date = event.getDateTime();

        mvc.perform(get(BookingController.ENDPOINT + "/price").session(session)
                .param("eventName", event.getName())
                .param("auditoriumName", auditorium.getName())
                .param("userId", String.valueOf(user.getId()))
                .param("localDateTime", date.toString())
                .param("seats", "1,2,3")
        )
                .andExpect(status().isOk());
    }

    @Test
    public void getTicketsForEvent() throws Exception {
        User user = testObjects.createBookingManager();
        MockHttpSession session = authenticateSession(user);

        Booking booking = testObjects.bookTicketToParty();
        Event event = booking.getTicket().getEvent();

        mvc.perform(get(BookingController.ENDPOINT + "/tickets")
                .session(session)
                .param("eventId", String.valueOf(event.getId())))
                .andExpect(status().isOk());
    }

}