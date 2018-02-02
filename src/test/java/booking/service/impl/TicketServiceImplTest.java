package booking.service.impl;

import booking.BaseServiceTest;
import booking.domain.Event;
import booking.domain.Ticket;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * @author Aleksey Yablokov
 */
public class TicketServiceImplTest extends BaseServiceTest {

    @Test
    public void testGetTicketsForEvent() {
        Ticket ticket = testObjects.createTicketToParty();
        Event event = ticket.getEvent();
        List<Ticket> ticketsForEvent = ticketService.getTicketsForEvent(event.getName(), event.getAuditorium().getId(), event.getDateTime());
        assertThat(ticketsForEvent, contains(ticket));
    }
}
