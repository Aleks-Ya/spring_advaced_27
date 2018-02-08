package booking.repository;

import booking.domain.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public interface EventDao {

    Event create(Event event);

    Event update(Event event);

    void delete(Event event);

    Event getById(Long eventId);

    List<Event> getByNameAndDate(String name, LocalDateTime dateTime);

    List<Event> getAll();

    List<Event> getForDateRange(LocalDateTime from, LocalDateTime to);

    List<Event> getNext(LocalDateTime to);

    Event getByAuditoriumAndDate(long auditoriumId, LocalDateTime date);

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
        if (Objects.isNull(event.getAuditorium())) {
            throw new NullPointerException("Events's auditorium is [null]. Event: [" + event + "]");
        }
    }
}
