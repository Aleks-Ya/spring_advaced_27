package booking.service;

import booking.domain.Ticket;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface TicketService {

    Ticket create(Ticket ticket);

    List<Ticket> getTicketsForEvent(long eventId);

    List<Ticket> getAll();

    Ticket getTicketById(Long ticketId);

    void delete(long ticketId);
}
