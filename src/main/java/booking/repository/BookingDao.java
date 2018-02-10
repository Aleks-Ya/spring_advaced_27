package booking.repository;

import booking.domain.Booking;
import booking.domain.Ticket;
import booking.domain.User;

import java.util.List;

public interface BookingDao {

    Booking create(long userId, Ticket ticket);

    Booking getById(long bookingId);

    List<Booking> getAll();

    void delete(User user, Ticket booking);

    void delete(long bookingId);

    long countTickets(long userId);

    List<Ticket> getTicketsForEvent(long eventId);
}
