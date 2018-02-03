package booking.web.controller;

import booking.BaseWebTest;
import booking.domain.*;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

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
@ContextConfiguration(classes = BookingController.class)
public class BookingControllerTest extends BaseWebTest {

    @Test
    public void getBookedTickets() throws Exception {
        Ticket ticket1 = createTicket();
        Ticket ticket2 = createTicket();
        mvc.perform(get(BookingController.ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().string(format(LoginControllerTest.ANONYMOUS_HEADER +
                                "<h1>Booked tickets</h1>\n" +
                                "<p>Ticket</p>\n" +
                                "<p>Id: %s</p>\n" +
                                "<p>Event: %s</p>\n" +
                                "<p>Date: 2017-01-15T10:30</p>\n" +
                                "<p>Seats: 1,2,3</p>\n" +
                                "<p>Price: 100</p><hr/>\n" +
                                "<p>Ticket</p>\n" +
                                "<p>Id: %s</p>\n" +
                                "<p>Event: %s</p>\n" +
                                "<p>Date: 2017-01-15T10:30</p>\n" +
                                "<p>Seats: 1,2,3</p>\n" +
                                "<p>Price: 100</p><hr/>\n",
                        ticket1.getId(), ticket1.getEvent().getName(),
                        ticket2.getId(), ticket2.getEvent().getName())));
    }

    @Test
    public void getTicketById() throws Exception {
        Ticket bookedTicket = createTicket();
        mvc.perform(get(BookingController.ENDPOINT + "/id/" + bookedTicket.getId())
        )
                .andExpect(status().isOk())
                .andExpect(content().string(format(LoginControllerTest.ANONYMOUS_HEADER +
                                "<h1>Ticket</h1>\n" +
                                "<p>Id: %s</p>\n" +
                                "<p>Event: %s</p>\n" +
                                "<p>Date: 2017-01-15T10:30</p>\n" +
                                "<p>Seats: 1,2,3</p>\n" +
                                "<p>Price: 100</p>",
                        bookedTicket.getId(), bookedTicket.getEvent().getName())));
    }

    @Test
    public void bookTicket() throws Exception {
        User user = createUser();
        Event event = createEvent();
        mvc.perform(post(BookingController.ENDPOINT)
                .param("userId", String.valueOf(user.getId()))
                .param("eventId", String.valueOf(event.getId()))
                .param("localDateTime", "2007-12-03T10:15:30")
                .param("seats", "1,2,3")
                .param("price", "100.5")
        )
                .andExpect(status().isCreated())
                .andExpect(content().string(format(
                        LoginControllerTest.ANONYMOUS_HEADER +
                                "<h1>The ticket is booked</h1>\n" +
                                "<p>Id: 1</p>\n" +
                                "<p>Event: %s</p>\n" +
                                "<p>Date: 2007-12-03T10:15:30</p>\n" +
                                "<p>Seats: 1,2,3</p>\n" +
                                "<p>Price: 100.5</p>",
                        event.getName())));
    }

    @Test
    public void getTicketPrice() throws Exception {
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
                .andExpect(content().string(
                        LoginControllerTest.ANONYMOUS_HEADER +
                                "<h1>Ticket price</h1>\n" +
                                "720\n"));
    }

    @Test
    public void getTicketsForEvent() throws Exception {
        Booking booking = testObjects.bookTicketToParty();
        Ticket ticket = booking.getTicket();
        Event event = booking.getTicket().getEvent();
        mvc.perform(get(BookingController.ENDPOINT + "/tickets")
                .param("eventId", String.valueOf(event.getId()))
        )
                .andExpect(status().isOk())
                .andExpect(content().string(format(
                        LoginControllerTest.ANONYMOUS_HEADER +
                                "<h1>Tickets for event</h1>\n" +
                                "<p>Event: %s</p>\n" +
                                "<p>Ticket</p>\n" +
                                "<p>Id: %d</p>\n" +
                                "<p>Event: %s</p>\n" +
                                "<p>Date: 2018-12-31T23:00</p>\n" +
                                "<p>Seats: 100,101</p>\n" +
                                "<p>Price: 10,000</p><hr/>\n",
                        event.getName(),
                        ticket.getId(),
                        event.getName()
                )));
    }

    private Ticket createTicket() {
        User user = createUser();
        Event event = createEvent();
        Ticket ticket = new Ticket(event, LocalDateTime.of(2017, 1, 15, 10, 30),
                Arrays.asList(1, 2, 3), 100);
        return bookingService.create(user.getId(), ticket).getTicket();
    }

    private Event createEvent() {
        return eventService.create(new Event(UUID.randomUUID() + "Meeting", Rate.HIGH, 100, null, null));
    }

    private User createUser() {
        User userObj = new User(UUID.randomUUID() + "mat@gmail.com", "Mat", null, "pass", null);
        return userService.register(userObj);
    }
}