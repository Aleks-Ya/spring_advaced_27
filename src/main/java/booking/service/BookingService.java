package booking.service;

import booking.domain.models.Ticket;
import booking.domain.models.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/3/2016
 * Time: 11:22 AM
 */
public interface BookingService {

    double getTicketPrice(String eventName, String auditoriumName, LocalDateTime dateTime, List<Integer> seats, User user);

    Ticket bookTicket(User user, Ticket ticket);

    List<Ticket> getTicketsForEvent(String eventName, Long auditoriumId, LocalDateTime date);

    List<Ticket> getBookedTickets();

    Ticket getTicketById(Long ticketId);
}
