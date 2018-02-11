package booking.web;

import booking.web.config.FreeMarkerConfig;
import booking.web.config.MessageConverterConfig;
import booking.web.config.MultipartConfig;
import booking.web.config.MvcConfig;
import booking.web.controller.ControllerConfig;
import booking.web.error.WebErrorHandler;
import booking.web.security.SecurityConfig;
import org.springframework.context.annotation.Import;

@Import({FreeMarkerConfig.class, MultipartConfig.class, MvcConfig.class, ControllerConfig.class, SecurityConfig.class,
        WebErrorHandler.class, MessageConverterConfig.class})
public class WebConfig {
}
