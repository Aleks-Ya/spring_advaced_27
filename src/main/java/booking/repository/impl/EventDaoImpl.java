package booking.repository.impl;

import booking.domain.Event;
import booking.repository.EventDao;
import org.hibernate.Query;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class EventDaoImpl extends AbstractDao implements EventDao {
    private static final Logger LOG = LoggerFactory.getLogger(EventDaoImpl.class);
    private static final String DATE_TIME_PROPERTY = "dateTime";

    @Override
    public Event create(Event event) {
        LOG.info("Creating {}", event);
        EventDao.validateEvent(event);
        Event byAuditoriumAndDate = getByAuditoriumAndDate(event.getAuditorium().getId(), event.getDateTime());
        if (byAuditoriumAndDate != null) {
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
    public void delete(Event event) {
        getCurrentSession().delete(event);
    }

    @Override
    public Optional<Event> getById(Long eventId) {
        return Optional.ofNullable((Event) createBlankCriteria(Event.class).add(Restrictions.eq("id", eventId)).uniqueResult());
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
    public Event getByAuditoriumAndDate(long auditoriumId, LocalDateTime date) {
        Query query = getCurrentSession().createQuery(
                "from Event e where e.auditorium.id = :auditoriumId and e.dateTime = :dateTime");
        query.setLong("auditoriumId", auditoriumId);
        query.setParameter(DATE_TIME_PROPERTY, date);
        return (Event) query.uniqueResult();
    }
}
