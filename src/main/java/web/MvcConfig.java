package web;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import web.controller.*;

/**
 * @author Aleksey Yablokov
 */
@Configuration
@EnableWebMvc
@Import({FreeMarkerConfig.class, AuditoriumController.class, BookingController.class, DiscountController.class,
        EventController.class, UserController.class, HelloWorldController.class})
public class MvcConfig {
}
