package booking.domain.daos.inmemory;

import booking.domain.daos.BookingDAO;
import booking.domain.models.Event;
import booking.domain.models.Ticket;
import booking.domain.models.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/4/2016
 * Time: 10:59 AM
 */
@Repository("inMemoryBookingDAO")
public class InMemoryBookingDAO implements BookingDAO {

    private static final Map<String, Set<Ticket>> db = new HashMap<>();

    @Override
    public Ticket create(User user, Ticket ticket) {
        BookingDAO.validateTicket(ticket);
        BookingDAO.validateUser(user);
        db.putIfAbsent(user.getEmail(), new HashSet<>());
        db.get(user.getEmail()).add(ticket);
        return ticket;
    }

    @Override
    public void delete(User user, Ticket ticket) {
        BookingDAO.validateTicket(ticket);
        BookingDAO.validateUser(user);
        final Set<Ticket> tickets = db.get(user.getEmail());
        if (Objects.nonNull(tickets))
            tickets.remove(ticket);
    }

    @Override
    public List<Ticket> getTickets(Event event) {
        if (Objects.isNull(event)) {
            throw new NullPointerException("Event is [null]");
        }
        return db.values().stream().flatMap(Collection:: stream).filter(ticket -> Objects.equals(ticket.getEvent(), event))
                 .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> getTickets(User user) {
        BookingDAO.validateUser(user);
        return db.getOrDefault(user.getEmail(), Collections.emptySet()).stream().collect(Collectors.toList());
    }

    @Override
    public long countTickets(User user) {
        return getTickets(user).size();
    }

    @Override
    public List<Ticket> getAllTickets() {
        return db.values().stream().flatMap(Collection:: stream).collect(Collectors.toList());
    }

    @Override
    public Optional<Ticket> getTicketById(Long ticketId) {
        if (ticketId == null) {
            return Optional.empty();
        }
        return db.values().stream()
                .flatMap(Collection::stream)
                .filter(ticket -> ticketId.equals(ticket.getId()))
                .findAny();
    }
}
