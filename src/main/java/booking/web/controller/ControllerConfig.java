package booking.web.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Aleksey Yablokov
 */
@Configuration
@Import({RootController.class, AuditoriumController.class, BookingController.class, BookingPdfController.class, DiscountController.class,
        EventController.class, PdfView.class, UserController.class})
public class ControllerConfig {
}
