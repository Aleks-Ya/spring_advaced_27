package beans.daos.db;

import beans.daos.AbstractDAO;
import beans.daos.BookingDAO;
import beans.models.Booking;
import beans.models.Event;
import beans.models.Ticket;
import beans.models.User;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 20/2/16
 * Time: 9:00 PM
 */
@Repository("bookingDAO")
public class BookingDAOImpl extends AbstractDAO implements BookingDAO {

    @Override
    public Ticket create(User user, Ticket ticket) {
        BookingDAO.validateTicket(ticket);
        BookingDAO.validateUser(user);

        Long ticketId = (Long) getCurrentSession().save(ticket);
        Ticket storedTicket = ticket.withId(ticketId);
        Booking booking = new Booking(user, storedTicket);
        getCurrentSession().save(booking);
        return storedTicket;
    }

    @Override
    public void delete(User user, Ticket ticket) {
        Query query = getCurrentSession().createQuery(
                "delete from Booking b where b.user = :user and b.ticket = :ticket");
        query.setParameter("user", user);
        query.setParameter("ticket", ticket);
        query.executeUpdate();
        getCurrentSession().delete(ticket);
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
    public List<Ticket> getAllTickets() {
        Query query = getCurrentSession().createQuery("select b.ticket from Booking b");
        return ((List<Ticket>) query.list());
    }
}
