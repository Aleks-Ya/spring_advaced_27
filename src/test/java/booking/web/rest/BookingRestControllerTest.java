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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = BookingRestController.class)
public class BookingRestControllerTest extends BaseWebTest {

    //TODO add test: booking not found by id
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

    //TODO add test: user not found by userId
    //TODO add test: event not found by eventId
    //TODO add test: seats already booked
    //TODO add test: price < 0
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
}