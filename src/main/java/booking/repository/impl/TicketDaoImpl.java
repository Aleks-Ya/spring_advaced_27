package booking.repository.impl;

import booking.domain.Booking;
import booking.domain.Event;
import booking.domain.Ticket;
import booking.domain.User;
import booking.repository.TicketDao;
import org.hibernate.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 20/2/16
 * Time: 9:00 PM
 */
public class TicketDaoImpl extends AbstractDAO implements TicketDao {

    @Override
    public Ticket create(Ticket ticket) {
        TicketDao.validateTicket(ticket);
        User user = ticket.getUser();
        TicketDao.validateUser(user);

        Long ticketId = (Long) getCurrentSession().save(ticket);
        Ticket storedTicket = ticket.withId(ticketId);
        Booking booking = new Booking(user, storedTicket);
        getCurrentSession().save(booking);
        return storedTicket;
    }

    @Override
    public void delete(long ticketId) {
        Query query = getCurrentSession().createQuery("delete from Ticket t where t.id = :ticketId");
        query.setParameter("ticketId", ticketId);
        query.executeUpdate();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ticket> getTickets(Event event) {
        Query query = getCurrentSession().createQuery("select b.ticket from Booking b where b.ticket.event = :event");
        query.setParameter("event", event);
        return ((List<Ticket>) query.list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ticket> getTickets(User user) {
        Query query = getCurrentSession().createQuery("select b.ticket from Booking b where b.user = :user");
        query.setParameter("user", user);
        return ((List<Ticket>) query.list());
    }

    @Override
    public long countTickets(User user) {
        Query query = getCurrentSession().createQuery("select count(*) from Booking b where b.user = :user");
        query.setParameter("user", user);
        return (Long) query.uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ticket> getBookedTickets() {
        Query query = getCurrentSession().createQuery("select b.ticket from Booking b");
        return ((List<Ticket>) query.list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ticket> getAll() {
        return ((List<Ticket>) createBlankCriteria(Ticket.class).list());
    }

    @Override
    public Optional<Ticket> getTicketById(Long ticketId) {
        Query query = getCurrentSession().createQuery("from Ticket t where t.id = :ticketId");
        query.setParameter("ticketId", ticketId);
        return Optional.ofNullable((Ticket) query.uniqueResult());
    }

}
