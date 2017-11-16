package booking.web.controller;

import booking.beans.models.Event;
import booking.beans.models.User;
import booking.beans.services.DiscountService;
import booking.beans.services.EventService;
import booking.beans.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Aleksey Yablokov
 */
@Controller
@RequestMapping(DiscountController.ENDPOINT)
public class DiscountController {
    static final String ENDPOINT = "/discount";
    static final String DISCOUNT_KEY = "discount";
    private static final String DISCOUNT_FTL = "auditorium/auditorium_vip_seats";

    @Autowired
    private DiscountService discountService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @SuppressWarnings("unused")
    ModelAndView getDiscount(@RequestParam Long userId, @RequestParam Long eventId, @ModelAttribute("model") ModelMap model) {
        User user = userService.getById(userId);
        Event event = eventService.getById(eventId);
        double discount = discountService.getDiscount(user, event);
        model.put(DISCOUNT_KEY, discount);
        return new ModelAndView(new PdfView(), model);
    }
}
