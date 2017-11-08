package beans.models;

import util.CsvUtil;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/1/2016
 * Time: 7:37 PM
 */
public class Ticket {

    private long          id;
    private Event         event;
    private LocalDateTime dateTime;
    private String        seats;
    private User          user;
    private Double        price;

    public Ticket() {
    }

    public Ticket(Event event, LocalDateTime dateTime, List<Integer> seats, User user, double price) {
        this(-1, event, dateTime, seats, user, price);
    }

    public Ticket(long id, Event event, LocalDateTime dateTime, List<Integer> seats, User user, Double price) {
        this(id, event, dateTime, CsvUtil.fromListToCsv(seats), user, price);
    }

    public Ticket(long id, Event event, LocalDateTime dateTime, String seats, User user, Double price) {
        this.id = id;
        this.event = event;
        this.dateTime = dateTime;
        this.user = user;
        this.price = price;
        this.seats = seats;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public void setSeatsList(List<Integer> seats) {
        this.seats = CsvUtil.fromListToCsv(seats);
    }

    public List<Integer> getSeatsList() {
        return CsvUtil.fromCsvToList(seats, Integer:: valueOf);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Ticket))
            return false;

        Ticket ticket = (Ticket) o;

        if (event != null ? !event.equals(ticket.event) : ticket.event != null)
            return false;
        if (dateTime != null ? !dateTime.equals(ticket.dateTime) : ticket.dateTime != null)
            return false;
        if (seats != null ? !seats.equals(ticket.seats) : ticket.seats != null)
            return false;
        if (user != null ? !user.equals(ticket.user) : ticket.user != null)
            return false;
        return price != null ? price.equals(ticket.price) : ticket.price == null;

    }

    @Override
    public int hashCode() {
        int result = event != null ? event.hashCode() : 0;
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (seats != null ? seats.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Ticket{" +
               "id=" + id +
               ", event=" + event +
               ", dateTime=" + dateTime +
               ", seats=" + seats +
               ", user=" + user +
               ", price=" + price +
               '}';
    }

    public Ticket withId(Long ticketId) {
        return new Ticket(ticketId, event, dateTime, seats, user, price);
    }
}
