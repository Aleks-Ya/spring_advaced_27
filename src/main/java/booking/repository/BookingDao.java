package booking.repository;

import booking.domain.Booking;
import booking.domain.Ticket;
import booking.domain.User;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface BookingDao {

    Booking create(User user, Ticket ticket);

    Booking getById(long bookingId);

    List<Booking> getAll();

    void delete(User user, Ticket booking);

    void delete(long bookingId);

    List<Ticket> getBookedTickets();

    long countTickets(User user);
}
