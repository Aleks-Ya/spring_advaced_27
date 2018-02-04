package booking.repository.impl;

import booking.domain.Auditorium;
import booking.domain.Event;
import booking.repository.EventDAO;
import org.hibernate.Query;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 20/2/16
 * Time: 7:07 PM
 */
@Repository
public class EventDAOImpl extends AbstractDAO implements EventDAO {
    private static final Logger LOG = LoggerFactory.getLogger(EventDAOImpl.class);
    private static final String DATE_TIME_PROPERTY = "dateTime";

    @Override
    public Event create(Event event) {
        LOG.info("Creating " + event);
        EventDAO.validateEvent(event);
        List<Event> byAuditoriumAndDate = getByAuditoriumAndDate(event.getAuditorium(), event.getDateTime());
        if (!byAuditoriumAndDate.isEmpty()) {
            throw new IllegalStateException(String.format(
                    "Unable to store event: [%s]. Event with such auditorium: [%s] on date: [%s] is already created.",
                    event, event.getAuditorium(), event.getDateTime()));
        } else {
            Long eventId = (Long) getCurrentSession().save(event);
            return event.withId(eventId);
        }
    }

    @Override
    public Event update(Event event) {
        return ((Event) getCurrentSession().merge(event));
    }

    @Override
    public Event get(String eventName, Auditorium auditorium, LocalDateTime dateTime) {
        LogicalExpression nameAndDate = Restrictions.and(Restrictions.eq(DATE_TIME_PROPERTY, dateTime),
                Restrictions.eq("name", eventName));
        return ((Event) createBlankCriteria(Event.class).add(nameAndDate).createAlias("auditorium", "aud").add(
                Restrictions.eq("aud.id", auditorium.getId())).uniqueResult());
    }

    @Override
    public void delete(Event event) {
        getCurrentSession().delete(event);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Event> getByName(String name) {
        return ((List<Event>) createBlankCriteria(Event.class).add(Restrictions.eq("name", name)).list());
    }

    @Override
    public Event getById(Long eventId) {
        return (Event) createBlankCriteria(Event.class).add(Restrictions.eq("id", eventId)).uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Event> getByNameAndDate(String name, LocalDateTime dateTime) {
        LogicalExpression nameAndDate = Restrictions.and(Restrictions.eq(DATE_TIME_PROPERTY, dateTime),
                Restrictions.eq("name", name));
        return ((List<Event>) createBlankCriteria(Event.class).add(nameAndDate).list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Event> getAll() {
        return ((List<Event>) createBlankCriteria(Event.class).list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Event> getForDateRange(LocalDateTime from, LocalDateTime to) {
        return ((List<Event>) createBlankCriteria(Event.class).add(Restrictions.between(DATE_TIME_PROPERTY, from, to)).list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Event> getNext(LocalDateTime to) {
        return ((List<Event>) createBlankCriteria(Event.class).add(Restrictions.le(DATE_TIME_PROPERTY, to)).list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Event> getByAuditoriumAndDate(Auditorium auditorium, LocalDateTime date) {
        Query query = getCurrentSession().createQuery(
                "from Event e where e.auditorium = :auditorium and e.dateTime = :dateTime");
        query.setParameter("auditorium", auditorium);
        query.setParameter(DATE_TIME_PROPERTY, date);
        return ((List<Event>) query.list());
    }
}
