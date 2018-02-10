package booking.service;

import booking.domain.Booking;
import booking.domain.Ticket;
import booking.domain.User;

import java.util.List;

/**
 * Join tickets and users.
 */
public interface BookingService {

    //TODO remove dateTime (already exists in Event)
    Booking bookTicket(long userId, long eventId, String seats, String dateTime, Double price);

    Booking getById(long bookingId);

    long countTickets(long userId);

    List<Ticket> getBookedTickets();

    List<Booking> getAll();

    void delete(long bookingId);

    double getTicketPrice(long eventId, List<Integer> seats, User user);

    List<Ticket> getTicketsForEvent(long eventId);
}
