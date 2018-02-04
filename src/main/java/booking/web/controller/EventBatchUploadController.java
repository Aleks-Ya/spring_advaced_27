package booking.web.controller;

import booking.domain.Auditorium;
import booking.domain.Event;
import booking.domain.Rate;
import booking.service.AuditoriumService;
import booking.service.EventService;
import booking.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static booking.web.controller.EventController.ENDPOINT;

/**
 * @author Aleksey Yablokov
 */
@Controller
@RequestMapping(ENDPOINT)
@SuppressWarnings("unused")
public class EventBatchUploadController {
    static final String PART_NAME = "events";

    private final EventService eventService;
    private final AuditoriumService auditoriumService;

    @Autowired
    public EventBatchUploadController(EventService eventService, AuditoriumService auditoriumService) {
        this.eventService = eventService;
        this.auditoriumService = auditoriumService;
    }

    @RequestMapping(path = "/batchUpload", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void batchUpload(@RequestParam(value = PART_NAME) List<MultipartFile> events) throws IOException {
        for (MultipartFile userFile : events) {
            List<EventCreateData> eventCreateDataList = JsonUtil.readValue(userFile.getBytes(), new TypeReference<List<EventCreateData>>() {
            });
            for (EventCreateData eventCreateData : eventCreateDataList) {
                Auditorium auditorium = auditoriumService.getById(eventCreateData.getAuditoriumId());
                Event event = new Event(eventCreateData.getId(), eventCreateData.getName(), eventCreateData.getRate(),
                        eventCreateData.getBasePrice(), eventCreateData.getDateTime(), auditorium);
                eventService.create(event);
            }
        }
    }

    private static class EventCreateData {
        private long id;
        private String name;
        private Rate rate;
        private double basePrice;
        private LocalDateTime dateTime;
        private Long auditoriumId;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Rate getRate() {
            return rate;
        }

        public void setRate(Rate rate) {
            this.rate = rate;
        }

        public double getBasePrice() {
            return basePrice;
        }

        public void setBasePrice(double basePrice) {
            this.basePrice = basePrice;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }

        public Long getAuditoriumId() {
            return auditoriumId;
        }

        public void setAuditoriumId(Long auditoriumId) {
            this.auditoriumId = auditoriumId;
        }
    }
}
