package booking.web.rest;

import booking.BaseWebTest;
import booking.domain.Booking;
import booking.domain.Event;
import booking.domain.User;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = BookingRestController.class)
public class BookingRestControllerTest extends BaseWebTest {

    @Test
    public void getById() throws Exception {
        Booking expBooking = to.bookTicketToParty();
        MvcResult mvcResult = mvc.perform(get(BookingRestController.ENDPOINT + "/" + expBooking.getId()))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Booking actBooking = objectMapper.readValue(content, Booking.class);

        expBooking.getUser().setPassword(null);
        assertThat(actBooking, equalTo(expBooking));
    }

    @Test
    public void getById_NotFound() throws Exception {
        mvc.perform(get(BookingRestController.ENDPOINT + "/123"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json("{'error':'Booking is not found by id 123'}"));
    }

    @Test
    public void getAll() throws Exception {
        Booking expBooking1 = to.bookTicketToParty();
        Booking expBooking2 = to.bookTicketToHackathon();
        MvcResult mvcResult = mvc.perform(get(BookingRestController.ENDPOINT))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        List<Booking> actBookings = objectMapper.readValue(content, new TypeReference<List<Booking>>() {
        });

        expBooking1.getUser().setPassword(null);
        expBooking2.getUser().setPassword(null);
        assertThat(actBookings, containsInAnyOrder(expBooking1, expBooking2));
    }

    @Test
    public void book() throws Exception {
        User user = to.createJohnWithAccount();
        Event event = to.createParty();
        MvcResult mvcResult = mvc.perform(post(BookingRestController.ENDPOINT)
                .param("userId", user.getId().toString())
                .param("eventId", event.getId().toString())
                .param("seats", "1,2")
        )
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Booking actBooking = objectMapper.readValue(content, Booking.class);

        Booking expBooking = bookingService.getById(actBooking.getId());
        expBooking.getUser().setPassword(null);
        assertThat(actBooking, equalTo(expBooking));
    }

    @Test
    public void book_userNotFound() throws Exception {
        Event event = to.createParty();
        mvc.perform(post(BookingRestController.ENDPOINT)
                .param("userId", "123")
                .param("eventId", event.getId().toString())
                .param("seats", "1,2")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(content().json("{error:'User is not found by id 123'}"));
    }

    @Test
    public void book_eventNotFound() throws Exception {
        User user = to.createJohnWithAccount();
        mvc.perform(post(BookingRestController.ENDPOINT)
                .param("userId", user.getId().toString())
                .param("eventId", "123")
                .param("seats", "1,2")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(content().json("{error:'Event is not found by id 123'}"));
    }

    @Test
    public void book_seatsAlreadyBooked() throws Exception {
        Booking booking = to.bookTicketToParty();
        mvc.perform(post(BookingRestController.ENDPOINT)
                .param("userId", booking.getUser().getId().toString())
                .param("eventId", booking.getTicket().getEvent().getId().toString())
                .param("seats", booking.getTicket().getSeats())
        )
                .andExpect(status().is4xxClientError())
                .andExpect(content().json("{error: \"Can't book seats 100,101 on 'New Year Party' because they are already booked.\"}"));
    }

    @Test
    public void book_negativePrice() throws Exception {
        User user = to.createJohnWithAccount();
        Event event = to.createParty();
        mvc.perform(post(BookingRestController.ENDPOINT)
                .param("userId", user.getId().toString())
                .param("eventId", event.getId().toString())
                .param("seats", "1,2")
                .param("price", "-1")
        )
                .andExpect(status().is4xxClientError())
                .andExpect(content().json("{error: \"Price '-1.0' is incorrect.\"}"));
    }
}