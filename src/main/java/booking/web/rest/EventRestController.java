package booking.web.rest;

import booking.domain.Event;
import booking.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static booking.web.rest.RestConfig.REST_ROOT_ENDPOINT;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@SuppressWarnings("unused")
class EventRestController {
    public static final String ENDPOINT = REST_ROOT_ENDPOINT + "/event";

    private final EventService eventService;

    @Autowired
    public EventRestController(EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping(path = ENDPOINT + "/{eventId}", method = GET)
    Event getById(@PathVariable Long eventId) {
        return eventService.getById(eventId);
    }

    @RequestMapping(path = ENDPOINT, method = GET)
    List<Event> getAll() {
        return eventService.getAll();
    }
}
