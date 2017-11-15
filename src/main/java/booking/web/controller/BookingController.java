package booking.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Aleksey Yablokov
 */
@Controller
@RequestMapping(BookingController.ENDPOINT)
public class BookingController {
    static final String ENDPOINT = "/booking";
}
