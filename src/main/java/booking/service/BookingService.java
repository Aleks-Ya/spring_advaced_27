package booking.service;

import booking.domain.Booking;
import booking.domain.Ticket;
import booking.domain.User;

import java.util.List;

/**
 * Join tickets and users.
 *
 * @author Aleksey Yablokov
 */
public interface BookingService {

    Booking create(User user, Ticket ticket);

    Booking getById(long bookingId);

    List<Booking> getAll();

    void delete(long bookingId);
}
