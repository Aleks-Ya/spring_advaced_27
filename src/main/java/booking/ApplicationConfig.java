package booking;

import booking.repository.RepositoryConfig;
import booking.service.ServiceConfig;
import booking.web.WebConfig;
import booking.web.rest.RestConfig;
import org.springframework.context.annotation.Import;

@Import({RepositoryConfig.class, ServiceConfig.class, WebConfig.class, RestConfig.class})
public class ApplicationConfig {
}
