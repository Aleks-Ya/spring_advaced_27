package beans.daos.inmemory;

import beans.daos.BookingDAO;
import beans.models.Event;
import beans.models.Ticket;
import beans.models.User;
import org.springframework.stereotype.Repository;

import java.util.*;
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
}
