package booking.web.controller;

import booking.beans.models.Auditorium;
import booking.beans.services.AuditoriumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static booking.web.controller.AuditoriumController.ENDPOINT;

/**
 * @author Aleksey Yablokov
 */
@Controller
@SuppressWarnings("unused")
@RequestMapping(value = ENDPOINT)
public class AuditoriumController {
    static final String ENDPOINT = "/auditorium";
    private static final String AUDITORIUMS_ATTR = "auditoriums";
    private static final String AUDITORIUM_ATTR = "auditorium";

    private static final String AUDITORIUMS_FTL = "auditorium/auditoriums";
    private static final String AUDITORIUM_FTL = "auditorium/auditorium";
    private static final String AUDITORIUM_SEATS_NUMBER_FTL = "auditorium/auditorium_seats_number";
    private static final String AUDITORIUM_VIP_SEATS_FTL = "auditorium/auditorium_vip_seats";
    private static final String AUDITORIUM_CREATED_FTL = "auditorium/auditorium_created";

    private final AuditoriumService auditoriumService;

    @Autowired
    public AuditoriumController(AuditoriumService auditoriumService) {
        this.auditoriumService = auditoriumService;
    }

    @RequestMapping(method = RequestMethod.PUT)
    String create(
            @RequestParam String auditoriumName,
            @RequestParam int seatsNumber,
            @RequestParam String vipSeats,
            @ModelAttribute("model") ModelMap model) {
        List<Integer> vipSeatList = SeatHelper.parseSeatsString(vipSeats);
        Auditorium auditorium = auditoriumService.create(new Auditorium(auditoriumName, seatsNumber, vipSeatList));
        model.addAttribute(AUDITORIUM_ATTR, auditorium);
        return AUDITORIUM_CREATED_FTL;
    }

    @RequestMapping(method = RequestMethod.GET)
    String getAuditoriums(@ModelAttribute("model") ModelMap model) {
        List<Auditorium> auditoriums = auditoriumService.getAuditoriums();
        model.addAttribute(AUDITORIUMS_ATTR, auditoriums);
        return AUDITORIUMS_FTL;
    }

    @RequestMapping("/id/{auditoriumId}")
    String getAuditoriumById(@PathVariable Long auditoriumId, @ModelAttribute("model") ModelMap model) {
        Auditorium auditorium = auditoriumService.getById(auditoriumId);
        model.addAttribute(AUDITORIUM_ATTR, auditorium);
        return AUDITORIUM_FTL;
    }

    @RequestMapping("/name/{auditoriumName}")
    String getAuditoriumByName(@PathVariable String auditoriumName, @ModelAttribute("model") ModelMap model) {
        Auditorium auditorium = auditoriumService.getByName(auditoriumName);
        model.addAttribute(AUDITORIUM_ATTR, auditorium);
        return AUDITORIUM_FTL;
    }

    @RequestMapping("/name/{auditoriumName}/seatsNumber")
    String getSeatsNumberByAuditoriumName(@PathVariable String auditoriumName, @ModelAttribute("model") ModelMap model) {
        Auditorium auditorium = auditoriumService.getByName(auditoriumName);
        model.addAttribute(AUDITORIUM_ATTR, auditorium);
        return AUDITORIUM_SEATS_NUMBER_FTL;
    }

    @RequestMapping("/id/{auditoriumId}/seatsNumber")
    String getSeatsNumberByAuditoriumId(@PathVariable Long auditoriumId, @ModelAttribute("model") ModelMap model) {
        Auditorium auditorium = auditoriumService.getById(auditoriumId);
        model.addAttribute(AUDITORIUM_ATTR, auditorium);
        return AUDITORIUM_SEATS_NUMBER_FTL;
    }

    @RequestMapping("/name/{auditoriumName}/vipSeats")
    String getVipSeatsByAuditoriumName(@PathVariable String auditoriumName, @ModelAttribute("model") ModelMap model) {
        Auditorium auditorium = auditoriumService.getByName(auditoriumName);
        model.addAttribute(AUDITORIUM_ATTR, auditorium);
        return AUDITORIUM_VIP_SEATS_FTL;
    }

    @RequestMapping("/id/{auditoriumId}/vipSeats")
    String getVipSeatsByAuditoriumId(@PathVariable Long auditoriumId, @ModelAttribute("model") ModelMap model) {
        Auditorium auditorium = auditoriumService.getById(auditoriumId);
        model.addAttribute(AUDITORIUM_ATTR, auditorium);
        return AUDITORIUM_VIP_SEATS_FTL;
    }
}
