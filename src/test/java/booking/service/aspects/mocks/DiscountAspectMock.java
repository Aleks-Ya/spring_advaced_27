package booking.service.aspects.mocks;

import booking.service.aspects.DiscountAspect;
import booking.service.impl.discount.BirthdayStrategy;
import booking.service.impl.discount.TicketsStrategy;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 13/2/16
 * Time: 8:38 PM
 */
@Deprecated
public class DiscountAspectMock extends DiscountAspect {
    public static void cleanup() {
        discountPerUserCounter.put(BirthdayStrategy.class.getSimpleName(), new HashMap<>());
        discountPerUserCounter.put(TicketsStrategy.class.getSimpleName(), new HashMap<>());
    }
}
