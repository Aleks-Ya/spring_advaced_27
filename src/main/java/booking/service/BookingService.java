package booking.service;

import booking.domain.Booking;
import booking.domain.Ticket;
import booking.domain.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Join tickets and users.
 *
 * @author Aleksey Yablokov
 */
public interface BookingService {

    Booking create(User user, Ticket ticket);

    Booking getById(long bookingId);

    List<Ticket> getBookedTickets();

    List<Booking> getAll();

    void delete(long bookingId);

    double getTicketPrice(String eventName, String auditoriumName, LocalDateTime dateTime, List<Integer> seats, User user);
}
