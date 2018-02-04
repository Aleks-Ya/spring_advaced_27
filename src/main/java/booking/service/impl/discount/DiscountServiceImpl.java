package booking.service.impl.discount;

import booking.domain.Event;
import booking.domain.User;
import booking.service.DiscountService;
import booking.service.DiscountStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/4/2016
 * Time: 11:23 AM
 */
@Service
public class DiscountServiceImpl implements DiscountService {

    private static final double MAX_DISCOUNT = 0.8;

    private final List<DiscountStrategy> strategies;

    /**
     * Lazy because of the circular dependency to {@link booking.service.impl.BookingServiceImpl}.
     */
    @Lazy
    @Autowired
    public DiscountServiceImpl(List<DiscountStrategy> strategies) {
        this.strategies = Collections.unmodifiableList(strategies);
    }

    @Override
    public double getDiscount(User user, Event event) {
        final Double discount = strategies.stream()
                .map(strategy -> strategy.calculateDiscount(user))
                .reduce((a, b) -> a + b)
                .orElse(0.0);
        return Double.compare(discount, MAX_DISCOUNT) > 0 ? MAX_DISCOUNT : discount;
    }
}
