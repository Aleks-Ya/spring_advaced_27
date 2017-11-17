package booking.web.controller;

import booking.beans.models.Event;
import booking.beans.services.EventService;
import booking.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@Controller
@RequestMapping("/event")
@SuppressWarnings("unused")
public class EventController {
    static final String PART_NAME = "events";
    private static final String EVENT_ATTR = "event";
    private static final String EVENTS_ATTR = "events";
    private static final String EVENT_CREATED_FTL = "event/event_created";
    private static final String EVENT_FTL = "event/event";
    private static final String EVENT_LIST_FTL = "event/event_list";

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping(method = RequestMethod.PUT)
    String create(@RequestBody Event newEvent, @ModelAttribute("model") ModelMap model) {
        Event event = eventService.create(newEvent);
        model.addAttribute(EVENT_ATTR, event);
        return EVENT_CREATED_FTL;
    }

    @RequestMapping(method = RequestMethod.GET)
    String getAll(@ModelAttribute("model") ModelMap model) {
        List<Event> events = eventService.getAll();
        model.addAttribute(EVENTS_ATTR, events);
        return EVENT_LIST_FTL;
    }

    @RequestMapping(path = "/id/{eventId}", method = RequestMethod.GET)
    String getById(@PathVariable Long eventId, @ModelAttribute("model") ModelMap model) {
        Event event = eventService.getById(eventId);
        model.addAttribute(EVENT_ATTR, event);
        return EVENT_FTL;
    }

    @RequestMapping(path = "/name/{eventName}", method = RequestMethod.GET)
    String getByName(@PathVariable String eventName, @ModelAttribute("model") ModelMap model) {
        List<Event> events = eventService.getByName(eventName);
        model.addAttribute(EVENTS_ATTR, events);
        return EVENT_LIST_FTL;
    }

    @RequestMapping(path = "/batchUpload", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void multipartUpload(@RequestParam(value = PART_NAME) List<MultipartFile> events) throws IOException {
        for (MultipartFile userFile : events) {
            Event event = JsonUtil.readValue(userFile.getBytes(), Event.class);
            eventService.create(event);
        }
    }
}
