package booking.web.controller;

import booking.beans.models.Auditorium;
import booking.beans.services.AuditoriumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@Controller
@RequestMapping("/auditorium")
public class AuditoriumController {

    @Autowired
    private AuditoriumService auditoriumService;

    @RequestMapping("auditoriums")
    @SuppressWarnings("unused")
    String getAuditoriums(@ModelAttribute("model") ModelMap model) {
        List<Auditorium> auditoriums = auditoriumService.getAuditoriums();
        model.addAttribute("auditoriums", auditoriums);
        return "auditoriums";
    }
}
