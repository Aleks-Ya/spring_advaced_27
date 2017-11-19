package booking.beans.services;

import booking.beans.models.Ticket;
import booking.beans.models.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/3/2016
 * Time: 11:22 AM
 */
public interface BookingService {

    double getTicketPrice(String event, String auditorium, LocalDateTime dateTime, List<Integer> seats, User user);

    Ticket bookTicket(User user, Ticket ticket);

    List<Ticket> getTicketsForEvent(String event, String auditorium, LocalDateTime date);

    List<Ticket> getBookedTickets();

    Ticket getTicketById(Long ticketId);
}
