package booking.service.impl;

import booking.BaseServiceTest;
import booking.domain.Booking;
import booking.domain.Event;
import booking.domain.Ticket;
import booking.domain.User;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class BookingServiceImplTest extends BaseServiceTest {

    @Test(expected = RuntimeException.class)
    public void testBookTicket_NotRegistered() {
        Event event = to.createParty();
        long notExistsUserId = 1;
        Booking expBooking = bookingService.bookTicket(notExistsUserId, event.getId(), "1", event.getBasePrice());
        assertThat(bookingService.getById(expBooking.getId()), equalTo(expBooking));
    }

    @Test(expected = RuntimeException.class)
    public void testBookTicket_AlreadyBooked() {
        User user = to.createJohn();
        Event event = to.createParty();
        String seats = "1,2";
        bookingService.bookTicket(user.getId(), event.getId(), seats, event.getBasePrice());
        bookingService.bookTicket(user.getId(), event.getId(), seats, event.getBasePrice());
    }

    @Test
    public void getAll() {
        Booking booking1 = to.bookTicketToParty();
        Booking booking2 = to.bookTicketToParty();
        assertThat(bookingService.getAll(), containsInAnyOrder(booking1, booking2));
    }

    @Test
    public void testGetTicketPrice_DiscountsForTicketsAndForBirthday() {
        User user = to.createJohnWithAccount();
        Booking booking = to.bookTicketToParty(user.getId());
        Event event = booking.getTicket().getEvent();
        double ticketPrice = bookingService.getTicketPrice(event.getId(), asList(5, 6, 7, 8), user);
        assertEquals("Price is wrong", 1200.0, ticketPrice, 0.00001);
    }

    @Test
    public void testGetTicketPrice_DiscountsForTicketsAndForBirthday_MidRate() {
        User user = to.createJohnWithAccount();
        Booking booking1 = to.bookTicketToParty(user.getId(), "5,6");
        to.bookTicketToParty(user.getId(), "5,6");
        Event event = booking1.getTicket().getEvent();
        double ticketPrice = bookingService.getTicketPrice(event.getId(), asList(5, 6, 7), user);
        assertEquals("Price is wrong", 960.0, ticketPrice, 0.00001);
    }

    @Test
    public void testGetTicketPrice() {
        Booking booking = to.bookTicketToParty();
        Ticket ticket = booking.getTicket();
        Event event = ticket.getEvent();
        User user = booking.getUser();
        double ticketPrice = bookingService.getTicketPrice(event.getId(), ticket.getSeatsList(), user);
        assertEquals("Price is wrong", 480.0, ticketPrice, 0.00001);
    }

    @Test
    public void testGetTicketPrice_WithoutDiscount() {
        Ticket ticket = to.createTicketToParty();
        User user = to.createJohn();
        Event event = ticket.getEvent();
        double ticketPrice = bookingService.getTicketPrice(event.getId(), ticket.getSeatsList(), user);
        assertEquals("Price is wrong", 480.0, ticketPrice, 0.00001);
    }

    @Test
    public void testGetTicketsForEvent() {
        Booking booking = to.bookTicketToParty();
        List<Ticket> ticketsForEvent = bookingService.getTicketsForEvent(booking.getTicket().getEvent().getId());
        assertThat(ticketsForEvent, contains(booking.getTicket()));
    }
}
