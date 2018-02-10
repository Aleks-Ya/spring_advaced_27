package booking.service.impl;

import booking.domain.Auditorium;
import booking.domain.Event;
import booking.repository.EventDao;
import booking.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static booking.exception.BookingExceptionFactory.notFoundById;


@Service
public class EventServiceImpl implements EventService {

    private final EventDao eventDao;

    @Autowired
    public EventServiceImpl(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public Event create(Event event) {
        return eventDao.create(event);
    }

    public void delete(Event event) {
        eventDao.delete(event);
    }

    @Override
    public Event getById(Long eventId) {
        return eventDao.getById(eventId).orElseThrow(() -> notFoundById(Event.class, eventId));
    }

    public Event getByAuditoriumAndDate(Auditorium auditorium, LocalDateTime dateTime) {
        return eventDao.getByAuditoriumAndDate(auditorium.getId(), dateTime);
    }

    public List<Event> getAll() {
        return eventDao.getAll();
    }

    public List<Event> getForDateRange(LocalDateTime from, LocalDateTime to) {
        return eventDao.getForDateRange(from, to);
    }

    public List<Event> getNextEvents(LocalDateTime to) {
        return eventDao.getNext(to);
    }

    public Event assignAuditorium(Event event, Auditorium auditorium, LocalDateTime date) {
        final Event updatedEvent = new Event(event.getId(), event.getName(), event.getRate(), event.getBasePrice(), date, auditorium);
        return eventDao.update(updatedEvent);
    }
}
