package beans.daos;

import beans.models.Auditorium;
import beans.models.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/2/2016
 * Time: 12:35 PM
 */
public interface EventDAO {

    Event create(Event event);

    Event update(Event event);

    Event get(String eventName, Auditorium auditoriumName, LocalDateTime dateTime);

    void delete(Event event);

    List<Event> getByName(String name);

    List<Event> getByNameAndDate(String name, LocalDateTime dateTime);

    List<Event> getAll();

    List<Event> getForDateRange(LocalDateTime from, LocalDateTime to);

    List<Event> getNext(LocalDateTime to);

    List<Event> getByAuditoriumAndDate(Auditorium auditorium, LocalDateTime date);

    static void validateEvent(Event event) {
        if (Objects.isNull(event)) {
            throw new NullPointerException("Event is [null]");
        }
        if (Objects.isNull(event.getName())) {
            throw new NullPointerException("Event's name is [null]. Event: [" + event + "]");
        }
        if (Objects.isNull(event.getRate())) {
            throw new NullPointerException("Events's rate is [null]. Event: [" + event + "]");
        }
    }
}
