package booking.repository;

import booking.domain.Booking;
import booking.domain.Ticket;
import booking.domain.User;

import java.util.List;
import java.util.Optional;

public interface BookingDao {

    Booking create(long userId, Ticket ticket);

    Optional<Booking> getById(long bookingId);

    List<Booking> getAll();

    void delete(User user, Ticket booking);

    void delete(long bookingId);

    long countTickets(long userId);

    List<Ticket> getTicketsForEvent(long eventId);
}
