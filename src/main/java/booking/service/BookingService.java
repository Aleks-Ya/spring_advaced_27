package booking.service;

import booking.domain.Booking;
import booking.domain.Ticket;

import java.util.List;

/**
 * Join tickets and users.
 */
public interface BookingService {

    Booking bookTicket(long userId, long eventId, String seats, Double price);

    Booking getById(long bookingId);

    long countTickets(long userId);

    List<Booking> getAll();

    void delete(long bookingId);

    double getTicketPrice(long eventId, List<Integer> seats, long userId);

    List<Ticket> getTicketsForEvent(long eventId);
}
