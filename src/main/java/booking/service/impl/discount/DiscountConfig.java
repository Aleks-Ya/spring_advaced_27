package booking.service.impl.discount;

import org.springframework.context.annotation.Import;

/**
 * @author Aleksey Yablokov
 */
@Import({DiscountServiceImpl.class, BirthdayStrategy.class, TicketsStrategy.class})
public class DiscountConfig {
}
