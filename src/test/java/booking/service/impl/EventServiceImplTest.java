package booking.service.impl;

import booking.BaseTest;
import booking.domain.Auditorium;
import booking.domain.Event;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static booking.domain.Rate.HIGH;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 1:23 PM
 */
public class EventServiceImplTest extends BaseTest {

    @Test
    public void testCreate() {
        Auditorium auditorium = testObjects.createBlueHall();
        Event event = eventService.create(new Event("Meeting", HIGH,
                1000, LocalDateTime.now(), auditorium));
        assertThat(eventService.getById(event.getId()), equalTo(event));
    }

    @Test
    public void testDelete() {
        Event party = testObjects.createParty();
        assertThat(eventService.getById(party.getId()), equalTo(party));
        eventService.delete(party);
        assertNull(eventService.getById(party.getId()));
    }

    @Test(expected = RuntimeException.class)
    public void testCreateSameEvent() {
        Event party = testObjects.createParty();
        eventService.create(party);
    }

    @Test
    public void testGetAll() {
        assertThat(eventService.getAll(), emptyIterable());
        Event party = testObjects.createParty();
        Event hackathon = testObjects.createHackathon();
        assertThat(eventService.getAll(), containsInAnyOrder(party, hackathon));
    }

    @Test
    public void testGetByName() {
        Event expParty = testObjects.createParty();
        List<Event> partyByName = eventService.getByName(expParty.getName());
        assertThat(partyByName, contains(expParty));
    }

    private Event getEvent(Event eventMock) {
        return eventService.getEvent(eventMock.getName(), eventMock.getAuditorium(), eventMock.getDateTime());
    }

    @Test
    public void testGetEvent() {
        Event event1 = testObjects.createParty();
        Event foundEvent = getEvent(event1);
        assertEquals("Events should match", event1.getAuditorium(), foundEvent.getAuditorium());
        assertEquals("Events should match", event1.getBasePrice(), foundEvent.getBasePrice(), 0);
        assertEquals("Events should match", event1.getDateTime(), foundEvent.getDateTime());
        assertEquals("Events should match", event1.getRate(), foundEvent.getRate());
        assertEquals("Events should match", event1.getName(), foundEvent.getName());
    }

    @Test
    public void testGetEvent_Exception() {
        Auditorium auditorium = new Auditorium(UUID.randomUUID().toString(), 1231, Collections.emptyList());
        Event event = eventService.getEvent(UUID.randomUUID().toString(), auditorium, LocalDateTime.now());
        assertNull("There shouldn't be such event in db", event);
    }

    @Test
    public void testAssignAuditorium_createNew() {
        Event oldEvent = testObjects.createParty();
        LocalDateTime newTime = LocalDateTime.now();
        Auditorium newAuditorium = testObjects.createRedHall();
        oldEvent.setAuditorium(newAuditorium);
        Event newEvent = eventService.assignAuditorium(oldEvent, newAuditorium, newTime);
        assertThat(eventService.getById(newEvent.getId()), equalTo(newEvent));
        assertThat(newEvent.getAuditorium(), equalTo(newAuditorium));
        assertThat(newEvent.getDateTime(), equalTo(newTime));
    }

    @Test
    public void testAssignAuditoriumAlreadyBusy() {
        Event party = testObjects.createParty();
        Event hackathon = testObjects.createHackathon();
        eventService.assignAuditorium(party, hackathon.getAuditorium(), hackathon.getDateTime());
    }

    @Test
    public void testGetById() {
        Event event = testObjects.createParty();
        assertThat(eventService.getById(event.getId()), equalTo(event));
    }

    @Test
    public void testGetByIdNotFound() {
        assertNull(eventService.getById(123456L));
    }
}
