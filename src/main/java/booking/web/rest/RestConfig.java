package booking.web.rest;

import org.springframework.context.annotation.Import;

@Import({UserRestController.class})
public class RestConfig {
    public static final String REST_ROOT_ENDPOINT = "/rest";
}
