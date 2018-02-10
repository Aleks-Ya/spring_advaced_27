package booking.repository;

import booking.domain.Event;
import booking.exception.BookingExceptionFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        boolean eventIsNull = Objects.isNull(event);
        boolean nameIsNull = Objects.isNull(event.getName());
        boolean auditoriumIsNull = Objects.isNull(event.getAuditorium());
        boolean rateIsNull = Objects.isNull(event.getRate());

        if (eventIsNull || nameIsNull || auditoriumIsNull || rateIsNull) {
            throw BookingExceptionFactory.incorrect(Event.class, event);
        }
    }
}
