package booking.web.controller;

import booking.beans.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Aleksey Yablokov
 */
@Controller
@SuppressWarnings("unused")
@RequestMapping(BookingController.ENDPOINT)
public class BookingController {
    static final String ENDPOINT = "/booking";
    private static final String TICKETS_ATTR = "tickets";
    private static final String TICKETS_FTL = "booking/booked_tickets";

    @Autowired
    private BookingService bookingService;

    @RequestMapping(method = RequestMethod.GET)
    String getBookedTickets(@ModelAttribute("model") ModelMap model) {
        model.addAttribute(TICKETS_ATTR, bookingService.getBookedTickets());
        return TICKETS_FTL;
    }
}
