package booking.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Aleksey Yablokov
 */
@Configuration
@EnableWebMvc
@ComponentScan("booking")
public class MvcConfig {
}
