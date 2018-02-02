package booking.service;

import booking.domain.Auditorium;
import booking.domain.Event;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/3/2016
 * Time: 11:02 AM
 */
public interface EventService {

    Event create(Event event);

    void delete(Event event);

    Event getEvent(String name, Auditorium auditorium, LocalDateTime dateTime);

    List<Event> getByName(String name);

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
