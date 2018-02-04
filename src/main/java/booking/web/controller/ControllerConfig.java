package booking.web.controller;

import org.springframework.context.annotation.Import;

@Import({RootController.class, LoginController.class, AuditoriumController.class, BookingController.class,
        BookingPdfController.class, DiscountController.class, EventController.class, PdfView.class,
        UserController.class, AccountController.class})
public class ControllerConfig {
    /**
     * Used in FreeMarker templates.
     */
    public static final String MODEL_ATTR = "model";
}
