package booking.service;

import booking.service.aspects.AspectConfig;
import booking.service.impl.AuditoriumServiceImpl;
import booking.service.impl.BookingServiceImpl;
import booking.service.impl.EventServiceImpl;
import booking.service.impl.UserServiceImpl;
import booking.service.impl.discount.DiscountConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Aleksey Yablokov
 */
@Configuration
@Import({DiscountConfig.class, AspectConfig.class,
        AuditoriumServiceImpl.class, BookingServiceImpl.class, EventServiceImpl.class, UserServiceImpl.class})
public class ServiceConfig {
}
