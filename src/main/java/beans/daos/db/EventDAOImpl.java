package beans.daos.db;

import beans.daos.AbstractDAO;
import beans.daos.EventDAO;
import beans.models.Auditorium;
import beans.models.Event;
import org.hibernate.Query;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 20/2/16
 * Time: 7:07 PM
 */
@Repository(value = "eventDAO")
public class EventDAOImpl extends AbstractDAO implements EventDAO {

    @Override
    public Event create(Event event) {
        System.out.println("Creating " + event);
        EventDAO.validateEvent(event);
        List<Event> byAuditoriumAndDate = getByAuditoriumAndDate(event.getAuditorium(), event.getDateTime());
        if (byAuditoriumAndDate.size() > 0) {
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
        LogicalExpression nameAndDate = Restrictions.and(Restrictions.eq("dateTime", dateTime),
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
    @SuppressWarnings("unchecked")
    public List<Event> getByNameAndDate(String name, LocalDateTime dateTime) {
        LogicalExpression nameAndDate = Restrictions.and(Restrictions.eq("dateTime", dateTime),
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
        return ((List<Event>) createBlankCriteria(Event.class).add(Restrictions.between("dateTime", from, to)).list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Event> getNext(LocalDateTime to) {
        return ((List<Event>) createBlankCriteria(Event.class).add(Restrictions.le("dateTime", to)).list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Event> getByAuditoriumAndDate(Auditorium auditorium, LocalDateTime date) {
        Query query = getCurrentSession().createQuery(
                "from Event e where e.auditorium = :auditorium and e.dateTime = :dateTime");
        query.setParameter("auditorium", auditorium);
        query.setParameter("dateTime", date);
        return ((List<Event>) query.list());
    }
}
