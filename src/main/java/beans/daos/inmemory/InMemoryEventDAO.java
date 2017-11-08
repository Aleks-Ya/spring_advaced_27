package beans.daos.inmemory;

import beans.daos.EventDAO;
import beans.models.Auditorium;
import beans.models.Event;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/3/2016
 * Time: 10:41 AM
 */
@Repository("inMemoryEventDAO")
public class InMemoryEventDAO implements EventDAO {

    private static final Map<String, List<Event>> db = new HashMap<>();

    @Override
    public Event create(Event event) {
        return safeUpdate(event);
    }

    @Override
    public Event update(Event event) {
        EventDAO.validateEvent(event);
        final List<Event> assignedEvents = getByAuditoriumAndDate(event.getAuditorium(), event.getDateTime());
        if (assignedEvents.isEmpty() || (assignedEvents.size() == 1 && Objects.equals(assignedEvents.get(0).getName(),
                                                                                      event.getName()))) {
            delete(new Event(event.getId(), event.getName(), event.getRate(), event.getBasePrice(), null, null));
            return create(event);
        } else
            throw new IllegalStateException(String.format(
                    "Unable to assign auditorium: [%s] for event: [%s] on date: [%s]. Auditorium is assigned for other events: [%s]",
                    event.getAuditorium(), event.getName(), event.getDateTime(), assignedEvents));
    }

    @Override
    public Event get(String name, Auditorium auditorium, LocalDateTime dateTime) {
        final Stream<Event> eventStream = getByName(name).stream();
        final Stream<Event> filteredByAuditorium = filterByAuditorium(eventStream, auditorium);
        final Stream<Event> filteredByDate = filterByDateTime(filteredByAuditorium, dateTime);
        return filteredByDate.findFirst().orElse(null);
    }

    @Override
    public void delete(Event event) {
        EventDAO.validateEvent(event);
        final List<Event> events = db.get(event.getName());
        if (Objects.nonNull(events)) {
            final List<Event> toRemove = events.stream().filter(foundEvent -> foundEvent.getId() == event.getId()).collect(
                    Collectors.toList());
            toRemove.forEach(events:: remove);
            if (events.isEmpty()) {
                db.remove(event.getName());
            }
        }
    }

    @Override
    public List<Event> getByName(String name) {
        if (Objects.isNull(name)) {
            throw new NullPointerException("Event name is [null]");
        }
        return db.getOrDefault(name, Collections.emptyList());
    }

    @Override
    public List<Event> getByNameAndDate(String name, LocalDateTime date) {
        if (Objects.isNull(name)) {
            throw new NullPointerException("Event name is [null]");
        }
        return getByName(name).stream().filter(event -> event.getDateTime().getYear() == date.getYear() &&
                                                        event.getDateTime().getMonth() == date.getMonth() &&
                                                        event.getDateTime().getDayOfMonth() == date.getDayOfMonth()).collect(
                Collectors.toList());
    }

    @Override
    public List<Event> getAll() {
        return getEventStream().collect(Collectors.toList());
    }

    @Override
    public List<Event> getForDateRange(LocalDateTime from, LocalDateTime to) {
        return getEventStream().filter(event -> event.getDateTime().isAfter(from) && event.getDateTime().isBefore(to)).collect(
                Collectors.toList());
    }

    @Override
    public List<Event> getNext(LocalDateTime to) {
        return getEventStream().filter(event -> event.getDateTime().isBefore(to)).collect(Collectors.toList());
    }

    @Override
    public List<Event> getByAuditoriumAndDate(Auditorium auditorium, LocalDateTime dateTime) {
        final Stream<Event> eventStream = getEventStream();
        final Stream<Event> filteredByDate = filterByDateTime(eventStream, dateTime);
        final Stream<Event> filteredByAuditorium = filterByAuditorium(filteredByDate, auditorium);
        return filteredByAuditorium.collect(Collectors.toList());
    }

    private Event safeUpdate(Event event) {
        EventDAO.validateEvent(event);
        final List<Event> assignedEvents = getByAuditoriumAndDate(event.getAuditorium(), event.getDateTime());
        if (assignedEvents.isEmpty()) {
            db.putIfAbsent(event.getName(), new ArrayList<>());
            db.get(event.getName()).add(event);
            return event;
        } else
            throw new IllegalStateException(String.format(
                    "Unable to assign auditorium: [%s] for event: [%s] on date: [%s]. Auditorium is assigned for other events: [%s]",
                    event.getAuditorium(), event.getName(), event.getDateTime(), assignedEvents));
    }

    private Stream<Event> getEventStream() {
        return db.values().stream().flatMap(Collection:: stream);
    }

    private Stream<Event> filterByAuditorium(Stream<Event> eventStream, Auditorium auditorium) {
        return filterBy(eventStream, event -> event.getAuditorium().getName(), auditorium.getName());
    }

    private Stream<Event> filterByDateTime(Stream<Event> eventStream, LocalDateTime dateTime) {
        return filterBy(eventStream, Event:: getDateTime, dateTime);
    }

    private <T> Stream<Event> filterBy(Stream<Event> eventStream, Function<Event, T> valueExtractor, T compareValue) {
        return Objects.isNull(compareValue) ? eventStream : eventStream.filter(
                event -> Objects.nonNull(valueExtractor.apply(event))).filter(
                event -> Objects.equals(valueExtractor.apply(event), compareValue));
    }
}
