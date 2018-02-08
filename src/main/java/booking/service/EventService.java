package booking.service;

import booking.domain.Auditorium;
import booking.domain.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    Event create(Event event);

    void delete(Event event);

    Event getEvent(long eventId, Auditorium auditorium, LocalDateTime dateTime);

    Event getById(Long eventId);

    List<Event> getAll();

    List<Event> getForDateRange(LocalDateTime from, LocalDateTime to);

    List<Event> getNextEvents(LocalDateTime to);

    /**
     * Change auditorium and date of an event.
     * Check that desired auditorium is free at that time.
     */
    Event assignAuditorium(Event event, Auditorium auditorium, LocalDateTime date);
}
