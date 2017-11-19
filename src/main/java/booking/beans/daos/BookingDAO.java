package booking.beans.daos;

import booking.beans.models.Event;
import booking.beans.models.Ticket;
import booking.beans.models.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/4/2016
 * Time: 10:21 AM
 */
public interface BookingDAO {

    Ticket create(User user, Ticket ticket);

    void delete(User user, Ticket booking);

    List<Ticket> getTickets(Event event);

    List<Ticket> getTickets(User user);

    long countTickets(User user);

    List<Ticket> getAllTickets();

    Optional<Ticket> getTicketById(Long ticketId);

    static void validateUser(User user) {
        if (Objects.isNull(user)) {
            throw new NullPointerException("User is [null]");
        }
        if (Objects.isNull(user.getEmail())) {
            throw new NullPointerException("User email is [null]");
        }
    }

    static void validateTicket(Ticket ticket) {
        if (Objects.isNull(ticket)) {
            throw new NullPointerException("Ticket is [null]");
        }
    }
}
