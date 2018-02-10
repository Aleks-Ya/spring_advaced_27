package booking.web.rest;

import booking.web.error.RestErrorHandler;
import org.springframework.context.annotation.Import;

@Import({UserRestController.class, EventRestController.class, BookingRestController.class, RestErrorHandler.class})
public class RestConfig {
    public static final String REST_ROOT_ENDPOINT = "/rest";
}
