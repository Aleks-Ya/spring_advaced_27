package booking.domain;

import booking.util.CsvUtil;

import java.time.LocalDateTime;
import java.util.List;

public class Ticket {

    private long id;
    private Event event;
    private LocalDateTime dateTime;//TODO remove (use Event#dateTime)
    private String seats;
    private Double price;

    public Ticket() {
    }

    public Ticket(Event event, LocalDateTime dateTime, List<Integer> seats, double price) {
        this(-1, event, dateTime, seats, price);
    }

    public Ticket(long id, Event event, LocalDateTime dateTime, List<Integer> seats, Double price) {
        this(id, event, dateTime, CsvUtil.fromListToCsv(seats), price);
    }

    public Ticket(long id, Event event, LocalDateTime dateTime, String seats, Double price) {
        this.id = id;
        this.event = event;
        this.dateTime = dateTime;
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
        if (dateTime != null ? !dateTime.equals(ticket.dateTime) : ticket.dateTime != null)
            return false;
        if (seats != null ? !seats.equals(ticket.seats) : ticket.seats != null)
            return false;
        return price != null ? price.equals(ticket.price) : ticket.price == null;

    }

    @Override
    public int hashCode() {
        int result = event != null ? event.hashCode() : 0;
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (seats != null ? seats.hashCode() : 0);
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
                ", price=" + price +
                '}';
    }

    public Ticket withId(Long ticketId) {
        return new Ticket(ticketId, event, dateTime, seats, price);
    }
}
