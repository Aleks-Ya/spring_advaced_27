package booking.service.impl;

import booking.BaseServiceTest;
import booking.domain.Auditorium;
import booking.domain.Event;
import booking.exception.BookingException;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static booking.domain.Rate.HIGH;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class EventServiceImplTest extends BaseServiceTest {

    @Test
    public void testCreate() {
        Auditorium auditorium = to.createBlueHall();
        Event event = eventService.create(new Event("Meeting", HIGH,
                1000, LocalDateTime.now(), auditorium));
        assertThat(eventService.getById(event.getId()), equalTo(event));
    }

    @Test
    public void testDelete() {
        Event party = to.createParty();
        assertThat(eventService.getById(party.getId()), equalTo(party));
        eventService.delete(party);
        assertThat(eventService.getAll(), emptyIterable());
    }

    @Test(expected = RuntimeException.class)
    public void testCreateSameEvent() {
        Event party = to.createParty();
        eventService.create(party);
    }

    @Test
    public void testGetAll() {
        assertThat(eventService.getAll(), emptyIterable());
        Event party = to.createParty();
        Event hackathon = to.createHackathon();
        assertThat(eventService.getAll(), containsInAnyOrder(party, hackathon));
    }

    private Event getEvent(Event eventMock) {
        return eventService.getByAuditoriumAndDate(eventMock.getAuditorium(), eventMock.getDateTime());
    }

    @Test
    public void testGetEvent() {
        Event event1 = to.createParty();
        Event foundEvent = getEvent(event1);
        assertEquals("Events should match", event1.getAuditorium(), foundEvent.getAuditorium());
        assertEquals("Events should match", event1.getBasePrice(), foundEvent.getBasePrice(), 0);
        assertEquals("Events should match", event1.getDateTime(), foundEvent.getDateTime());
        assertEquals("Events should match", event1.getRate(), foundEvent.getRate());
        assertEquals("Events should match", event1.getName(), foundEvent.getName());
    }

    @Test
    public void testGetEvent_Exception() {
        Auditorium auditorium = new Auditorium(1L, "Big Hall", 1231, Collections.emptyList());
        Event event = eventService.getByAuditoriumAndDate(auditorium, LocalDateTime.now());
        assertNull("There shouldn't be such event in db", event);
    }

    @Test
    public void testAssignAuditorium_createNew() {
        Event oldEvent = to.createParty();
        LocalDateTime newTime = LocalDateTime.now();
        Auditorium newAuditorium = to.createRedHall();
        oldEvent.setAuditorium(newAuditorium);
        Event newEvent = eventService.assignAuditorium(oldEvent, newAuditorium, newTime);
        assertThat(eventService.getById(newEvent.getId()), equalTo(newEvent));
        assertThat(newEvent.getAuditorium(), equalTo(newAuditorium));
        assertThat(newEvent.getDateTime(), equalTo(newTime));
    }

    @Test
    public void testAssignAuditoriumAlreadyBusy() {
        Event party = to.createParty();
        Event hackathon = to.createHackathon();
        eventService.assignAuditorium(party, hackathon.getAuditorium(), hackathon.getDateTime());
    }

    @Test
    public void testGetById() {
        Event event = to.createParty();
        assertThat(eventService.getById(event.getId()), equalTo(event));
    }

    @Test(expected = BookingException.class)
    public void testGetByIdNotFound() {
        assertNull(eventService.getById(123456L));
    }
}
