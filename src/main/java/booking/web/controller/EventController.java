package booking.web.controller;

import booking.domain.Auditorium;
import booking.domain.Event;
import booking.domain.Rate;
import booking.service.AuditoriumService;
import booking.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@SuppressWarnings("unused")
public class EventController {
    static final String ENDPOINT = "/event";

    private static final String EVENT_ATTR = "event";
    private static final String EVENTS_ATTR = "events";

    private static final String EVENT_CREATED_FTL = "event/event_created";
    private static final String EVENT_FTL = "event/event";
    private static final String EVENT_LIST_FTL = "event/event_list";

    private final EventService eventService;
    private final AuditoriumService auditoriumService;

    @Autowired
    public EventController(EventService eventService, AuditoriumService auditoriumService) {
        this.eventService = eventService;
        this.auditoriumService = auditoriumService;
    }

    @RequestMapping(path = ENDPOINT, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    String create(@RequestParam String name,
                  @RequestParam String rate,
                  @RequestParam Double bastPrice,
                  @RequestParam String dateTime,
                  @RequestParam Long auditoriumId,
                  @ModelAttribute(ControllerConfig.MODEL_ATTR) ModelMap model) {
        Auditorium auditorium = auditoriumService.getById(auditoriumId);
        Event event = new Event(name, Rate.valueOf(rate), bastPrice, LocalDateTime.parse(dateTime), auditorium);
        Event eventCreated = eventService.create(event);
        model.addAttribute(EVENT_ATTR, eventCreated);
        return EVENT_CREATED_FTL;
    }

    @RequestMapping(path = ENDPOINT, method = RequestMethod.GET)
    String getAll(@ModelAttribute(ControllerConfig.MODEL_ATTR) ModelMap model) {
        List<Event> events = eventService.getAll();
        model.addAttribute(EVENTS_ATTR, events);
        return EVENT_LIST_FTL;
    }

    @RequestMapping(path = ENDPOINT + "/id/{eventId}", method = RequestMethod.GET)
    String getById(@PathVariable Long eventId, @ModelAttribute(ControllerConfig.MODEL_ATTR) ModelMap model) {
        Event event = eventService.getById(eventId);
        model.addAttribute(EVENT_ATTR, event);
        return EVENT_FTL;
    }
}
