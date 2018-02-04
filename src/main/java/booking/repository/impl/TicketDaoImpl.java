package booking.repository.impl;

import booking.domain.Ticket;
import booking.repository.TicketDao;
import org.hibernate.Query;

import java.util.List;
import java.util.Optional;

public class TicketDaoImpl extends AbstractDao implements TicketDao {

    @Override
    public Ticket create(Ticket ticket) {
        TicketDao.validateTicket(ticket);
        getCurrentSession().save(ticket);
        return ticket;
    }

    @Override
    public void delete(long ticketId) {
        Query query = getCurrentSession().createQuery("delete from Ticket t where t.id = :ticketId");
        query.setParameter("ticketId", ticketId);
        query.executeUpdate();
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
