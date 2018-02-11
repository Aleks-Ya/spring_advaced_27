package booking.web.controller;

import org.springframework.context.annotation.Import;

@Import({RootController.class, LoginController.class, AuditoriumController.class, BookingController.class,
        DiscountController.class, EventController.class, UserController.class, AccountController.class})
public class ControllerConfig {
    /**
     * Used in FreeMarker templates.
     */
    public static final String MODEL_ATTR = "model";
}
