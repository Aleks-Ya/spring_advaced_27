package booking.web;

import booking.web.config.FreeMarkerConfig;
import booking.web.config.MultipartConfig;
import booking.web.config.MvcConfig;
import booking.web.controller.ControllerConfig;
import booking.web.error.AdviceErrorHandler;
import booking.web.security.SecurityConfig;
import org.springframework.context.annotation.Import;

/**
 * @author Aleksey Yablokov
 */
@Import({FreeMarkerConfig.class, MultipartConfig.class, MvcConfig.class, ControllerConfig.class, SecurityConfig.class,
        AdviceErrorHandler.class})
public class WebConfig {
}
