package booking.service.aspects.mocks;

import booking.service.aspects.DiscountAspect;
import booking.service.impl.discount.BirthdayStrategy;
import booking.service.impl.discount.TicketsStrategy;

import java.util.HashMap;

public class DiscountAspectMock extends DiscountAspect {
    public static void cleanup() {
        discountPerUserCounter.put(BirthdayStrategy.class.getSimpleName(), new HashMap<>());
        discountPerUserCounter.put(TicketsStrategy.class.getSimpleName(), new HashMap<>());
    }
}
