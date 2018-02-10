package booking.domain;

import booking.util.CsvUtil;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Event event;
    private String seats;
    private Double price;

    public Ticket() {
    }

    public Ticket(Event event, List<Integer> seats, double price) {
        this(null, event, seats, price);
    }

    public Ticket(Long id, Event event, List<Integer> seats, Double price) {
        this(id, event, CsvUtil.fromListToCsv(seats), price);
    }

    public Ticket(Long id, Event event, String seats, Double price) {
        this.id = id;
        this.event = event;
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
        return CsvUtil.fromCsvToList(seats, Integer::valueOf);
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
        if (seats != null ? !seats.equals(ticket.seats) : ticket.seats != null)
            return false;
        return price != null ? price.equals(ticket.price) : ticket.price == null;

    }

    @Override
    public int hashCode() {
        int result = event != null ? event.hashCode() : 0;
        result = 31 * result + (seats != null ? seats.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", event=" + event +
                ", seats=" + seats +
                ", price=" + price +
                '}';
    }
}
