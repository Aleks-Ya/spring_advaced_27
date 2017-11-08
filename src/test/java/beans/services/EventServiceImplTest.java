package beans.services;

import beans.configuration.AppConfiguration;
import beans.configuration.TestEventServiceConfiguration;
import beans.configuration.db.DataSourceConfiguration;
import beans.configuration.db.DbSessionFactory;
import beans.daos.mocks.EventDAOMock;
import beans.models.Auditorium;
import beans.models.Event;
import beans.models.Rate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 1:23 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfiguration.class, DataSourceConfiguration.class, DbSessionFactory.class,
                                 TestEventServiceConfiguration.class})
@Transactional
public class EventServiceImplTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    @Value("#{testEventServiceImpl}")
    private EventService eventService;

    @Autowired
    @Value("#{testHall1}")
    Auditorium auditorium;

    @Autowired
    @Qualifier("testHall2")
    Auditorium auditorium2;

    private final Event testEvent = new Event(UUID.randomUUID().toString(), Rate.HIGH, 1321, LocalDateTime.now(),
                                                     null);

    @Autowired
    @Value("#{testEventDAOMock}")
    private EventDAOMock eventDAOMock;

    @Before
    public void init() {
        System.out.println("!!!");
        testEvent.setAuditorium(auditorium);
        eventDAOMock.init();
        System.out.println("$$$");
    }

    @After
    public void clean() {
        System.out.println("***");
        eventDAOMock.cleanup();
        System.out.println("###");
    }

    @Test
    public void testCreate() throws Exception {
        List<Event> before = eventService.getAll();
        eventService.create(testEvent);
        List<Event> after = eventService.getAll();
        before.add(testEvent);
        assertTrue("Events should change", after.containsAll(before));
        assertTrue("Events should change", before.containsAll(after));
    }

    @Test
    public void testRemove() throws Exception {
        List<Event> before = eventService.getAll();
        Event eventMock = (Event) applicationContext.getBean("testEvent1");
        Event event = getEvent(eventMock);
        eventService.remove(event);
        List<Event> after = eventService.getAll();
        before.remove(event);
        assertEquals("Events should change", after, before);
    }

    @Test(expected = RuntimeException.class)
    public void testCreate_Exception() throws Exception {
        Event addedEvent = (Event) applicationContext.getBean("testEvent1");
        eventService.create(addedEvent);
    }

    @Test
    public void testGetAll() throws Exception {
        List<Event> all = eventService.getAll();
        Event event1 = (Event) applicationContext.getBean("testEvent1");
        Event event2 = (Event) applicationContext.getBean("testEvent2");
        Event event3 = (Event) applicationContext.getBean("testEvent3");
        List<Event> expected = Arrays.asList(getEvent(event1), getEvent(event2), getEvent(event3));
        System.out.println(all);
        assertTrue("List of events should match", expected.containsAll(all));
        assertTrue("List of events should match", all.containsAll(expected));
    }

    @Test
    public void testGetByName() throws Exception {
        Event eventMock = (Event) applicationContext.getBean("testEvent1");
        Event event1 = getEvent(eventMock);
        Event eventMock3 = (Event) applicationContext.getBean("testEvent3");
        Event event3 = getEvent(eventMock3);
        List<Event> foundByName = eventService.getByName(event1.getName());
        List<Event> expected = Arrays.asList(event1, event3);
        assertTrue("List of events should match", expected.containsAll(foundByName));
        assertTrue("List of events should match", foundByName.containsAll(expected));
    }

    private Event getEvent(Event eventMock) {return eventService.getEvent(eventMock.getName(), eventMock.getAuditorium(), eventMock.getDateTime());}

    @Test
    public void testGetEvent() throws Exception {
        Event event1 = (Event) applicationContext.getBean("testEvent1");
        Event foundEvent = getEvent(event1);
        assertEquals("Events should match", event1.getAuditorium(), foundEvent.getAuditorium());
        assertEquals("Events should match", event1.getBasePrice(), foundEvent.getBasePrice());
        assertEquals("Events should match", event1.getDateTime(), foundEvent.getDateTime());
        assertEquals("Events should match", event1.getRate(), foundEvent.getRate());
        assertEquals("Events should match", event1.getName(), foundEvent.getName());
    }

    @Test
    public void testGetEvent_Exception() throws Exception {
        Auditorium auditorium = new Auditorium(UUID.randomUUID().toString(), 1231, Collections.emptyList());
        Event event = eventService.getEvent(UUID.randomUUID().toString(), auditorium, LocalDateTime.now());
        assertNull("There shouldn't be such event in db", event);
    }

    @Test
    public void testAssignAuditorium_createNew() throws Exception {
        System.out.println("auditorium = " + auditorium);
        System.out.println("auditorium2 = " + auditorium2);
        List<Event> before = eventService.getAll();
        Event event = eventService.create(testEvent);
        System.out.println("event = " + event);
        eventService.assignAuditorium(event, auditorium2, event.getDateTime());
        List<Event> after = eventService.getAll();
        before.add(testEvent);
        assertTrue("Events should match", before.containsAll(after));
        assertTrue("Events should match", after.containsAll(before));
    }

    @Test
    public void testAssignAuditorium_update() throws Exception {
        List<Event> before = eventService.getAll();
        Event eventMock = (Event) applicationContext.getBean("testEvent1");
        Event event = getEvent(eventMock);
        eventService.assignAuditorium(event, testEvent.getAuditorium(), testEvent.getDateTime());
        List<Event> after = eventService.getAll();
        before.remove(event);
        before.add(new Event(event.getId(), event.getName(), event.getRate(), event.getBasePrice(),
                             testEvent.getDateTime(), testEvent.getAuditorium()));
        System.out.println("before = " + before);
        System.out.println("after = " + after);
        assertTrue("Events should match", before.containsAll(after));
        assertTrue("Events should match", after.containsAll(before));
    }

    @Test
    public void testAssignAuditorium_Exception() throws Exception {
        Event event1 = (Event) applicationContext.getBean("testEvent1");
        Event event2 = (Event) applicationContext.getBean("testEvent2");
        eventService.assignAuditorium(event1, event2.getAuditorium(), event2.getDateTime());
    }
}
