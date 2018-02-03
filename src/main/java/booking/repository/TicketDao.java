package booking.repository;

import booking.domain.Event;
import booking.domain.Ticket;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/4/2016
 * Time: 10:21 AM
 */
public interface TicketDao {

    Ticket create(Ticket ticket);

    void delete(long ticketId);

    /**
     * TODO Move to BookingDao
     * TODO get eventId
     */
    List<Ticket> getTickets(Event event);

    List<Ticket> getAll();

    Optional<Ticket> getTicketById(Long ticketId);

    static void validateTicket(Ticket ticket) {
        if (Objects.isNull(ticket)) {
            throw new NullPointerException("Ticket is [null]");
        }
    }
}
