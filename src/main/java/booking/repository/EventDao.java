package booking.repository;

import booking.domain.Event;
import booking.exception.BookingExceptionFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

public interface EventDao {

    Event create(Event event);

    Event update(Event event);

    void delete(Event event);

    Optional<Event> getById(Long eventId);

    List<Event> getByNameAndDate(String name, LocalDateTime dateTime);

    List<Event> getAll();

    List<Event> getForDateRange(LocalDateTime from, LocalDateTime to);

    List<Event> getNext(LocalDateTime to);

    Event getByAuditoriumAndDate(long auditoriumId, LocalDateTime date);

    static void validateEvent(Event event) {
        if (isNull(event) || isNull(event.getName()) || isNull(event.getAuditorium()) || isNull(event.getRate())) {
            throw BookingExceptionFactory.incorrect(Event.class, event);
        }
    }
}
