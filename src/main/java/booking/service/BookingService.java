package booking.service;

import booking.domain.Booking;
import booking.domain.Ticket;
import booking.domain.User;

import java.util.List;

/**
 * Join tickets and users.
 */
public interface BookingService {

    Booking bookTicket(long userId, long eventId, String seats, Double price);

    Booking getById(long bookingId);

    long countTickets(long userId);

    //TODO remove (the same as getAll().stream().map(booking -> booking.getTicket())
    List<Ticket> getBookedTickets();

    List<Booking> getAll();

    void delete(long bookingId);

    double getTicketPrice(long eventId, List<Integer> seats, User user);

    List<Ticket> getTicketsForEvent(long eventId);
}
