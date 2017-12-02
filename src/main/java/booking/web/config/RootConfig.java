package booking.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Aleksey Yablokov
 */
@Configuration
@ComponentScan("booking")
@EnableAspectJAutoProxy
public class RootConfig {
}
