package booking.service;

import booking.domain.User;

public interface DiscountStrategy {

    double calculateDiscount(User user);
}
