package booking.repository.mocks;

import booking.domain.Event;
import booking.domain.Ticket;
import booking.domain.User;
import booking.repository.TicketDao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 3:08 AM
 */
public class TicketDaoDiscountMock implements TicketDao {

    public final String userThatBookedTickets;
    public final int    ticketsBought;

    public TicketDaoDiscountMock(String userThatBookedTickets, int ticketsBought) {
        this.userThatBookedTickets = userThatBookedTickets;
        this.ticketsBought = ticketsBought;
    }

    @Override
    public Ticket create(User user, Ticket ticket) {
        return null;
    }

    @Override
    public void delete(User user, Ticket booking) {

    }

    @Override
    public List<Ticket> getTickets(Event event) {
        return null;
    }

    @Override
    public List<Ticket> getTickets(User user) {
        return null;
    }

    @Override
    public long countTickets(User user) {
        return Objects.equals(user.getName(), userThatBookedTickets) ? ticketsBought : 0;
    }

    @Override
    public List<Ticket> getAllTickets() {
        return null;
    }

    @Override
    public Optional<Ticket> getTicketById(Long ticketId) {
        return Optional.empty();
    }
}
