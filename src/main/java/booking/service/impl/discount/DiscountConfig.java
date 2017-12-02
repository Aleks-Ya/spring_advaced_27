package booking.service.impl.discount;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Aleksey Yablokov
 */
@Configuration
@Import({DiscountServiceImpl.class, BirthdayStrategy.class, TicketsStrategy.class})
public class DiscountConfig {
}
