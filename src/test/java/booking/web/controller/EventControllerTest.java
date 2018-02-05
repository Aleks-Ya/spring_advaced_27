package booking.web.controller;

import booking.BaseWebTest;
import booking.domain.Auditorium;
import booking.domain.Event;
import booking.domain.Rate;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import static java.lang.String.format;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = EventController.class)
public class EventControllerTest extends BaseWebTest {

    @Test
    public void create() throws Exception {
        Auditorium auditorium = testObjects.createBlueHall();
        String name = "Discussion";

        assertThat(eventService.getByName(name), emptyIterable());

        mvc.perform(post(EventController.ENDPOINT)
                .param("id", "1")
                .param("name", name)
                .param("rate", "HIGH")
                .param("bastPrice", "10.5")
                .param("dateTime", "2007-12-03T10:15:30")
                .param("auditoriumId", String.valueOf(auditorium.getId()))
        )
                .andExpect(status().isCreated())
                .andExpect(content().string(matchesPattern(".*\n" +
                        "<h1>Event created</h1>\n" +
                        "<p>Id: \\d+</p>\n" +
                        "<p>Name: Discussion</p>\n" +
                        "<p>Rate: HIGH</p>\n" +
                        "<p>Base price: 10.5</p>\n" +
                        "<p>Date: 2007-12-03T10:15:30</p>\n" +
                        "<p>Auditorium: Blue hall</p>\n")));

        assertThat(eventService.getByName(name), hasSize(1));
    }

    @Test
    public void getById() throws Exception {
        Event event = eventService.create(new Event("Meeting", Rate.HIGH, 100, null, null));

        mvc.perform(get(EventController.ENDPOINT + "/id/" + event.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(format(LoginControllerTest.ANONYMOUS_HEADER +
                        "<h1>Event</h1>\n" +
                        "<p>Id: %s</p>\n" +
                        "<p>Name: Meeting</p>\n" +
                        "<p>Rate: HIGH</p>\n" +
                        "<p>Base price: 100</p>\n" +
                        "<p>Date: -No date-</p>\n" +
                        "<p>Auditorium: -No auditorium-</p>\n", event.getId())));
    }

    @Test
    public void getByName() throws Exception {
        String eventName = "Kick Off";
        Event event1 = eventService.create(new Event(eventName, Rate.HIGH, 100, null, null));
        Event event2 = eventService.create(new Event(eventName, Rate.HIGH, 100, null, null));

        mvc.perform(get(EventController.ENDPOINT + "/name/" + eventName))
                .andExpect(status().isOk())
                .andExpect(content().string(format(LoginControllerTest.ANONYMOUS_HEADER +
                        RootControllerTest.NAVIGATOR +
                        "<h1>Event list</h1>\n" +
                        "<p>Event</p>\n" +
                        "<p>Id: %s</p>\n" +
                        "<p>Name: Kick Off</p>\n" +
                        "<p>Rate: HIGH</p>\n" +
                        "<p>Base price: 100</p>\n" +
                        "<p>Date: -No date-</p>\n" +
                        "<p>Auditorium: -No auditorium-</p>\n" +
                        "<hr/>\n" +
                        "<p>Event</p>\n" +
                        "<p>Id: %s</p>\n" +
                        "<p>Name: Kick Off</p>\n" +
                        "<p>Rate: HIGH</p>\n" +
                        "<p>Base price: 100</p>\n" +
                        "<p>Date: -No date-</p>\n" +
                        "<p>Auditorium: -No auditorium-</p>\n" +
                        "<hr/>\n", event1.getId(), event2.getId())));
    }

    @Test
    public void getAll() throws Exception {
        eventService.getAll().forEach(event -> eventService.delete(event));
        String eventName = "Travel";
        Event event1 = eventService.create(new Event(eventName, Rate.HIGH, 100, null, null));
        Event event2 = eventService.create(new Event(eventName, Rate.HIGH, 100, null, null));

        mvc.perform(get(EventController.ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().string(format(LoginControllerTest.ANONYMOUS_HEADER +
                        RootControllerTest.NAVIGATOR +
                        "<h1>Event list</h1>\n" +
                        "<p>Event</p>\n" +
                        "<p>Id: %s</p>\n" +
                        "<p>Name: Travel</p>\n" +
                        "<p>Rate: HIGH</p>\n" +
                        "<p>Base price: 100</p>\n" +
                        "<p>Date: -No date-</p>\n" +
                        "<p>Auditorium: -No auditorium-</p>\n" +
                        "<hr/>\n" +
                        "<p>Event</p>\n" +
                        "<p>Id: %s</p>\n" +
                        "<p>Name: Travel</p>\n" +
                        "<p>Rate: HIGH</p>\n" +
                        "<p>Base price: 100</p>\n" +
                        "<p>Date: -No date-</p>\n" +
                        "<p>Auditorium: -No auditorium-</p>\n" +
                        "<hr/>\n", event1.getId(), event2.getId())));
    }
}