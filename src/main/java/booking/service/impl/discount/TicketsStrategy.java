package booking.service.impl.discount;

import booking.domain.User;
import booking.service.BookingService;
import booking.service.DiscountStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/4/2016
 * Time: 11:25 AM
 */
@Component
@PropertySource({"classpath:strategies/strategies.properties"})
public class TicketsStrategy implements DiscountStrategy {

    private final double ticketsDiscountValue;
    private final double defaultDiscount;
    private final int discountThreshold;
    private final BookingService bookingService;

    @Autowired
    public TicketsStrategy(
            BookingService bookingService,
            @Value("${tickets.discount}") double ticketsDiscountValue,
            @Value("${tickets.discount.threshold}") int discountThreshold,
            @Value("${tickets.discount.default}") double defaultDiscount
    ) {
        this.bookingService = bookingService;
        this.ticketsDiscountValue = ticketsDiscountValue;
        this.discountThreshold = discountThreshold;
        this.defaultDiscount = defaultDiscount;
    }

    @Override
    public double calculateDiscount(User user) {
        final long boughtTicketsCount = bookingService.countTickets(user.getId());
        if ((boughtTicketsCount + 1) % discountThreshold == 0) {
            return ticketsDiscountValue;
        }
        return defaultDiscount;
    }
}
