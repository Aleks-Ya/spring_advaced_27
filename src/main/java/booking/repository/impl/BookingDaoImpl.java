package booking.repository.impl;

import booking.domain.Booking;
import booking.domain.Ticket;
import booking.domain.User;
import booking.repository.BookingDao;
import booking.repository.TicketDao;
import org.hibernate.Query;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public class BookingDaoImpl extends AbstractDAO implements BookingDao {

    @Override
    public Booking create(User user, Ticket ticket) {
        TicketDao.validateTicket(ticket);
        TicketDao.validateUser(user);

        Long ticketId = (Long) getCurrentSession().save(ticket);
        Ticket storedTicket = ticket.withId(ticketId);
        Booking booking = new Booking(user, storedTicket);
        getCurrentSession().save(booking);
        return booking;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Booking> getAll() {
        return ((List<Booking>) createBlankCriteria(Booking.class).list());
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
    public void delete(long bookingId) {
        Query query = getCurrentSession().createQuery(
                "delete from Booking b where b.id = :bookingId");
        query.setParameter("bookingId", bookingId);
        query.executeUpdate();
    }
}
