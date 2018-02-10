package booking.repository;

import booking.domain.Ticket;
import booking.exception.BookingExceptionFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface TicketDao {

    Ticket create(Ticket ticket);

    void delete(long ticketId);

    List<Ticket> getAll();

    Optional<Ticket> getTicketById(Long ticketId);

    static void validateTicket(Ticket ticket) {
        if (Objects.isNull(ticket)) {
            throw BookingExceptionFactory.incorrect(Ticket.class, ticket);
        }
    }
}
