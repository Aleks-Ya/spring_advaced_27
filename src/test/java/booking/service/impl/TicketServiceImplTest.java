package booking.service.impl;

import booking.BaseTest;
import booking.domain.Event;
import booking.domain.Ticket;
import booking.domain.User;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Aleksey Yablokov
 */
public class TicketServiceImplTest extends BaseTest {

    @Test
    public void testGetTicketsForEvent() {
        Ticket ticket = testObjects.createTicketToParty();
        Event event = ticket.getEvent();
        List<Ticket> ticketsForEvent = ticketService.getTicketsForEvent(event.getName(), event.getAuditorium().getId(), event.getDateTime());
        assertThat(ticketsForEvent, contains(ticket));
    }

    @Ignore("move to BookingService") //TODO
    @Test
    public void testGetTicketPrice() {
        Ticket ticket = testObjects.createTicketToParty();
        Event event = ticket.getEvent();
        double ticketPrice = ticketService.getTicketPrice(event.getName(), event.getAuditorium().getName(),
                event.getDateTime(), ticket.getSeatsList(),
                ticket.getUser());
        assertEquals("Price is wrong", 297.6, ticketPrice, 0.00001);
    }

    @Ignore("move to BookingService") //TODO
    @Test
    public void testGetTicketPrice_WithoutDiscount() {
        Ticket ticket = testObjects.createTicketToParty();
        User user = ticket.getUser();
        Event event = ticket.getEvent();
        double ticketPrice = ticketService.getTicketPrice(event.getName(), event.getAuditorium().getName(),
                event.getDateTime(), ticket.getSeatsList(), user);
        assertEquals("Price is wrong", 595.2, ticketPrice, 0.00001);
    }

}
