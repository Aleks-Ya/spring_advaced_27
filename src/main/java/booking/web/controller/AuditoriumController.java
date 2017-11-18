package booking.web.controller;

import booking.beans.models.Auditorium;
import booking.beans.services.AuditoriumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@Controller
@SuppressWarnings("unused")
@RequestMapping(value = "/auditorium", method = RequestMethod.GET)
public class AuditoriumController {
    private static final String AUDITORIUMS_ATTRIBUTE = "auditoriums";
    private static final String AUDITORIUM_ATTRIBUTE = "auditorium";
    private static final String AUDITORIUMS_FTL = "auditorium/auditoriums";
    private static final String AUDITORIUM_FTL = "auditorium/auditorium";
    private static final String AUDITORIUM_SEATS_NUMBER_FTL = "auditorium/auditorium_seats_number";
    private static final String AUDITORIUM_VIP_SEATS_FTL = "auditorium/auditorium_vip_seats";

    private final AuditoriumService auditoriumService;

    @Autowired
    public AuditoriumController(AuditoriumService auditoriumService) {
        this.auditoriumService = auditoriumService;
    }

    @RequestMapping
    String getAuditoriums(@ModelAttribute("model") ModelMap model) {
        List<Auditorium> auditoriums = auditoriumService.getAuditoriums();
        model.addAttribute(AUDITORIUMS_ATTRIBUTE, auditoriums);
        return AUDITORIUMS_FTL;
    }

    @RequestMapping("/id/{auditoriumId}")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    String getAuditoriumById(@PathVariable String auditoriumId) {
        return null; //TODO implement
    }

    @RequestMapping("/name/{auditoriumName}")
    String getAuditoriumByName(@PathVariable String auditoriumName, @ModelAttribute("model") ModelMap model) {
        Auditorium auditorium = auditoriumService.getByName(auditoriumName);
        model.addAttribute(AUDITORIUM_ATTRIBUTE, auditorium);
        return AUDITORIUM_FTL;
    }

    @RequestMapping("/name/{auditoriumName}/seatsNumber")
    String getSeatsNumberByAuditoriumName(@PathVariable String auditoriumName, @ModelAttribute("model") ModelMap model) {
        Auditorium auditorium = auditoriumService.getByName(auditoriumName);
        model.addAttribute(AUDITORIUM_ATTRIBUTE, auditorium);
        return AUDITORIUM_SEATS_NUMBER_FTL;
    }

    @RequestMapping("/id/{auditoriumId}/seatsNumber")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    String getSeatsNumberByAuditoriumId(@PathVariable String auditoriumId, @ModelAttribute("model") ModelMap model) {
        return null; //TODO implement
    }

    @RequestMapping("/name/{auditoriumName}/vipSeats")
    String getVipSeatsByAuditoriumName(@PathVariable String auditoriumName, @ModelAttribute("model") ModelMap model) {
        Auditorium auditorium = auditoriumService.getByName(auditoriumName);
        model.addAttribute(AUDITORIUM_ATTRIBUTE, auditorium);
        return AUDITORIUM_VIP_SEATS_FTL;
    }

    @RequestMapping("/id/{auditoriumId}/vipSeats")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    String getVipSeatsByAuditoriumId(@PathVariable String auditoriumId, @ModelAttribute("model") ModelMap model) {
        return null; //TODO implement
    }
}
