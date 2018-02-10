package booking.service;

import booking.domain.Booking;
import booking.domain.Ticket;
import booking.domain.User;

import java.util.List;

/**
 * Join tickets and users.
 */
public interface BookingService {

    //TODO throws UserNotFoundException
    //TODO throws EventNotFoundException
    //TODO throws SeatsAlreadyBookedException
    //TODO throws IncorrectPriceException
    Booking bookTicket(long userId, long eventId, String seats, Double price);

    //TODO throws BookingNotFoundException
    Booking getById(long bookingId);

    long countTickets(long userId);//TODO throws UserNotFoundException

    List<Booking> getAll();

    void delete(long bookingId);//TODO throws BookingNotFoundException

    //TODO accept userId
    //TODO throws EventNotFoundException
    //TODO throws UserNotFoundException
    double getTicketPrice(long eventId, List<Integer> seats, User user);

    //TODO throws EventNotFoundException
    List<Ticket> getTicketsForEvent(long eventId);
}
