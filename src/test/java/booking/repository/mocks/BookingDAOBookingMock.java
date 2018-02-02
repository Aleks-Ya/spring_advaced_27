package booking.repository.mocks;

import booking.domain.Ticket;
import booking.domain.User;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 3:08 AM
 */
@Deprecated
public class BookingDAOBookingMock
//        extends TicketDaoImpl
{

    private final Map<User, Set<Ticket>> initWith;

    public BookingDAOBookingMock(Map<User, Set<Ticket>> initWith) {
        this.initWith = initWith;
    }

    public void init() {
        cleanup();
        System.out.println("creating " + initWith);
        throw new UnsupportedOperationException();
//        initWith.forEach((user, tickets) -> tickets.forEach(ticket -> create(user, ticket)));
    }

    public void cleanup() {
        throw new UnsupportedOperationException();
//        getBookedTickets().forEach(ticket -> delete(ticket.getUser(), ticket));
    }
}
