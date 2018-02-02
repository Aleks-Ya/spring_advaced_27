package booking.service.impl;

import booking.BaseTest;
import booking.domain.Booking;
import booking.domain.Event;
import booking.domain.Ticket;
import booking.domain.User;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 8:28 PM
 */
@Ignore("fix it") //TODO
public class BookingServiceImplTest extends BaseTest {

    @Test(expected = RuntimeException.class)
    public void testBookTicket_NotRegistered() {
        Ticket ticket = testObjects.createTicketToParty();
        User user = testObjects.createJohn();
        Booking expBooking = bookingService.create(user, ticket);
        assertThat(bookingService.getById(expBooking.getId()), equalTo(expBooking));
    }

    @Test(expected = RuntimeException.class)
    public void testBookTicket_AlreadyBooked() {
        Ticket newTicket = testObjects.createTicketToParty();
        User user = newTicket.getUser();
        bookingService.create(user, newTicket);
        bookingService.create(user, newTicket);
    }

    @Test
    public void getAll() {
        Event testEvent1 = testObjects.createParty();
        Ticket newTicket = testObjects.createTicketToHackathon();
        User testUser1 = newTicket.getUser();
        Booking booking = bookingService.create(testUser1, newTicket);

        assertThat(bookingService.getAll(), containsInAnyOrder(booking));
    }

    @Test
    public void testGetTicketPrice_DiscountsForTicketsAndForBirthday() {
        Ticket ticket = testObjects.createTicketToParty();
        Ticket ticket2 = testObjects.createTicketToHackathon();
        User testUser = ticket.getUser();
        User registeredUser = testObjects.createJohn();
        bookingService.create(registeredUser, ticket);
        bookingService.create(ticket2.getUser(), ticket2);
        Event event = ticket.getEvent();
        double ticketPrice = ticketService.getTicketPrice(event.getName(), event.getAuditorium().getName(),
                event.getDateTime(), Arrays.asList(5, 6, 7, 8),
                registeredUser);
        assertEquals("Price is wrong", 260.4, ticketPrice, 0.00001);
    }

    @Test
    public void testGetTicketPrice_DiscountsForTicketsAndForBirthday_MidRate() {
        Ticket ticket = testObjects.createTicketToParty();
        User testUser = ticket.getUser();
        bookingService.create(testUser, ticket);
        bookingService.create(testUser, ticket);
        Event event = ticket.getEvent();
        double ticketPrice = ticketService.getTicketPrice(event.getName(), event.getAuditorium().getName(),
                event.getDateTime(), Arrays.asList(5, 6, 7), testUser);
        assertEquals("Price is wrong", 525, ticketPrice, 0.00001);
    }
}
