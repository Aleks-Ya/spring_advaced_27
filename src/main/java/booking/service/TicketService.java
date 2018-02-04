package booking.service;

import booking.domain.Ticket;

import java.util.List;

public interface TicketService {

    Ticket create(Ticket ticket);

    List<Ticket> getAll();

    Ticket getTicketById(Long ticketId);

    void delete(long ticketId);
}
