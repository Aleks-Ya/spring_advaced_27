package booking.service.impl.discount;

import booking.domain.User;
import booking.repository.BookingDao;
import booking.repository.TicketDao;
import booking.service.DiscountStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/4/2016
 * Time: 11:25 AM
 */
@Component
@PropertySource({"classpath:strategies/strategies.properties"})
@Transactional
public class TicketsStrategy implements DiscountStrategy {

    public final double ticketsDiscountValue;
    public final double defaultDiscount;
    private final TicketDao ticketDao;
    private final int discountThreshold;
    private final BookingDao bookingDao;

    @Autowired
    public TicketsStrategy(
            TicketDao ticketDao,
            @Value("${tickets.discount}") double ticketsDiscountValue,
            @Value("${tickets.discount.threshold}") int discountThreshold,
            @Value("${tickets.discount.default}") double defaultDiscount, BookingDao bookingDao) {
        this.ticketDao = ticketDao;
        this.ticketsDiscountValue = ticketsDiscountValue;
        this.discountThreshold = discountThreshold;
        this.defaultDiscount = defaultDiscount;
        this.bookingDao = bookingDao;
    }

    @Override
    public double calculateDiscount(User user) {
        final long boughtTicketsCount = bookingDao.countTickets(user);
        if ((boughtTicketsCount + 1) % discountThreshold == 0) {
            return ticketsDiscountValue;
        }
        return defaultDiscount;
    }
}
