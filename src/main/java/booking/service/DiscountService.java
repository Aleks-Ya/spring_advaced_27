package booking.service;

import booking.domain.Event;
import booking.domain.User;

public interface DiscountService {

    double getDiscount(User user, Event event);
}
