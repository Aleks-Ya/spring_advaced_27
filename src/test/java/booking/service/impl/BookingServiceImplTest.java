package booking.service.impl;

import booking.BaseServiceTest;
import booking.domain.Booking;
import booking.domain.Event;
import booking.domain.Ticket;
import booking.domain.User;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class BookingServiceImplTest extends BaseServiceTest {

    @Test(expected = RuntimeException.class)
    public void testBookTicket_NotRegistered() {
        Ticket ticket = testObjects.createTicketToParty();
        long notExistsUserId = 1;
        Booking expBooking = bookingService.bookTicket(notExistsUserId, ticket);
        assertThat(bookingService.getById(expBooking.getId()), equalTo(expBooking));
    }

    @Test(expected = RuntimeException.class)
    public void testBookTicket_AlreadyBooked() {
        Ticket newTicket = testObjects.createTicketToParty();
        User user = testObjects.createJohn();
        bookingService.bookTicket(user.getId(), newTicket);
        bookingService.bookTicket(user.getId(), newTicket);
    }

    @Test
    public void getAll() {
        Ticket newTicket = testObjects.createTicketToHackathon();
        User user = testObjects.createJohnWithAccount();
        Booking booking = bookingService.bookTicket(user.getId(), newTicket);

        assertThat(bookingService.getAll(), containsInAnyOrder(booking));
    }

    @Test
    public void testGetTicketPrice_DiscountsForTicketsAndForBirthday() {
        Ticket ticket = testObjects.createTicketToParty();
        Ticket ticket2 = testObjects.createTicketToHackathon();
        User user = testObjects.createJohnWithAccount();
        User registeredUser = testObjects.createJohnWithAccount();
        bookingService.bookTicket(registeredUser.getId(), ticket);
        bookingService.bookTicket(user.getId(), ticket2);
        Event event = ticket.getEvent();
        double ticketPrice = bookingService.getTicketPrice(event.getName(), event.getAuditorium().getName(),
                event.getDateTime(), Arrays.asList(5, 6, 7, 8),
                registeredUser);
        assertEquals("Price is wrong", 1200.0, ticketPrice, 0.00001);
    }

    @Test
    public void testGetTicketPrice_DiscountsForTicketsAndForBirthday_MidRate() {
        Ticket ticket = testObjects.createTicketToParty();
        Ticket ticket2 = testObjects.createTicketToHackathon();
        User user = testObjects.createJohnWithAccount();
        bookingService.bookTicket(user.getId(), ticket);
        bookingService.bookTicket(user.getId(), ticket2);
        Event event = ticket.getEvent();
        double ticketPrice = bookingService.getTicketPrice(event.getName(), event.getAuditorium().getName(),
                event.getDateTime(), Arrays.asList(5, 6, 7), user);
        assertEquals("Price is wrong", 960.0, ticketPrice, 0.00001);
    }

    @Test
    public void testGetTicketPrice() {
        Booking booking = testObjects.bookTicketToParty();
        Ticket ticket = booking.getTicket();
        Event event = ticket.getEvent();
        User user = booking.getUser();
        double ticketPrice = bookingService.getTicketPrice(event.getName(), event.getAuditorium().getName(),
                event.getDateTime(), ticket.getSeatsList(),
                user);
        assertEquals("Price is wrong", 480.0, ticketPrice, 0.00001);
    }

    @Test
    public void testGetTicketPrice_WithoutDiscount() {
        Ticket ticket = testObjects.createTicketToParty();
        User user = testObjects.createJohn();
        Event event = ticket.getEvent();
        double ticketPrice = bookingService.getTicketPrice(event.getName(), event.getAuditorium().getName(),
                event.getDateTime(), ticket.getSeatsList(), user);
        assertEquals("Price is wrong", 480.0, ticketPrice, 0.00001);
    }

    @Test
    public void testGetTicketsForEvent() {
        Booking booking = testObjects.bookTicketToParty();
        List<Ticket> ticketsForEvent = bookingService.getTicketsForEvent(booking.getTicket().getEvent().getId());
        assertThat(ticketsForEvent, contains(booking.getTicket()));
    }
}
