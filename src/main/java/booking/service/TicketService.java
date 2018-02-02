package booking.service;

import booking.domain.Ticket;
import booking.domain.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface TicketService {

    Ticket create(Ticket ticket);

    /**
     * TODO move to BookingService
     */
    double getTicketPrice(String eventName, String auditoriumName, LocalDateTime dateTime, List<Integer> seats, User user);

    List<Ticket> getTicketsForEvent(String eventName, Long auditoriumId, LocalDateTime date);

    /**
     * TODO move to BookingService
     */
    List<Ticket> getBookedTickets();

    List<Ticket> getAll();

    Ticket getTicketById(Long ticketId);

    void delete(long ticketId);
}
