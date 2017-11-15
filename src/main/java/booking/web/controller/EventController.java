package booking.web.controller;

import booking.beans.models.Event;
import booking.beans.services.EventService;
import booking.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@Controller
@RequestMapping("/event")
public class EventController {
    static final String PART_NAME = "events";

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
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
