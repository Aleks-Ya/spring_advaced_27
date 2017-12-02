package booking.web.controller;

import booking.domain.Event;
import booking.domain.User;
import booking.service.DiscountService;
import booking.service.EventService;
import booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Aleksey Yablokov
 */
@Controller
@SuppressWarnings("unused")
@RequestMapping(value = DiscountController.ENDPOINT)
public class DiscountController {
    static final String ENDPOINT = "/discount";
    static final String DISCOUNT_KEY = "discount";
    private static final String DISCOUNT_FTL = "discount/discount";

    private final DiscountService discountService;

    private final UserService userService;

    private final EventService eventService;

    @Autowired
    public DiscountController(DiscountService discountService, UserService userService, EventService eventService) {
        this.discountService = discountService;
        this.userService = userService;
        this.eventService = eventService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getDiscount(@RequestParam Long userId, @RequestParam Long eventId, @ModelAttribute("model") ModelMap model) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User is not found by id=" + userId);
        }
        Event event = eventService.getById(eventId);
        if (event == null) {
            throw new IllegalArgumentException("Event is not found by id=" + eventId);
        }
        double discount = discountService.getDiscount(user, event);
        model.put(DISCOUNT_KEY, discount);
        return DISCOUNT_FTL;
    }
}
