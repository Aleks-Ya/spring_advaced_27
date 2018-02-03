package booking.service.impl;

import booking.BaseServiceTest;
import booking.domain.Booking;
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
        Booking booking = testObjects.bookTicketToParty();
        List<Ticket> ticketsForEvent = ticketService.getTicketsForEvent(booking.getTicket().getEvent().getId());
        assertThat(ticketsForEvent, contains(booking.getTicket()));
    }
}
