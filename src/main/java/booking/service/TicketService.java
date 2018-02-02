package booking.service;

import booking.domain.Ticket;
import booking.domain.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface TicketService {
    double getTicketPrice(String eventName, String auditoriumName, LocalDateTime dateTime, List<Integer> seats, User user);

    List<Ticket> getTicketsForEvent(String eventName, Long auditoriumId, LocalDateTime date);

    List<Ticket> getBookedTickets();

    List<Ticket> getAll();

    Ticket getTicketById(Long ticketId);

    void delete(long ticketId);
}
