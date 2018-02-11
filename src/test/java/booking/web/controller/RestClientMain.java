package booking.web.controller;

import booking.domain.Booking;
import booking.domain.Event;
import booking.domain.Ticket;
import booking.domain.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST client for Booking Application.
 * Run main server (mvn jetty:run) before executing this class.
 */
public class RestClientMain {
    private static final String ROOT_URL = "http://localhost:8080/rest";
    private static final RestTemplate template = new RestTemplate();

    public static void main(String[] args) {
        List<User> users = loadUsers();
        List<Event> events = loadEvents();

        User user = users.get(0);
        Event event = events.get(0);
        List<Ticket> tickets = getTicketsByEvent(event.getId());
        Integer nextFreeSeat = tickets.stream()
                .map(Ticket::getSeatsList)
                .flatMap(Collection::stream)
                .min(Comparator.reverseOrder())
                .get() + 1;
        book(user.getId(), event.getId(), nextFreeSeat.toString());
        getAllBookings();
    }

    private static List<User> loadUsers() {
        String endpoint = ROOT_URL + "/user";
        ResponseEntity<List<User>> entity = template.exchange(endpoint, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<User>>() {
                });
        List<User> users = entity.getBody();
        System.out.println("\nAll users:\n" + listToString(users) + "\n");
        return users;
    }

    private static List<Ticket> getTicketsByEvent(long eventId) {
        String endpoint = UriComponentsBuilder.fromHttpUrl(ROOT_URL + "/booking/tickets_for_event")
                .queryParam("eventId", eventId)
                .toUriString();
        ResponseEntity<List<Ticket>> entity = template.exchange(endpoint, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Ticket>>() {
                });
        List<Ticket> tickets = entity.getBody();
        System.out.println("\nTickets for event:\n" + listToString(tickets) + "\n");
        return tickets;
    }

    private static List<Event> loadEvents() {
        String endpoint = ROOT_URL + "/event";
        ResponseEntity<List<Event>> entity = template.exchange(endpoint, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Event>>() {
                });
        List<Event> events = entity.getBody();
        System.out.println("\nAll events:\n" + listToString(events) + "\n");
        return events;
    }

    private static List<Booking> getAllBookings() {
        String endpoint = ROOT_URL + "/booking";
        ResponseEntity<List<Booking>> entity = template.exchange(endpoint, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Booking>>() {
                });
        List<Booking> bookings = entity.getBody();
        System.out.println("\nAll bookings:\n" + listToString(bookings) + "\n");
        return bookings;
    }

    private static Booking book(long userId, long eventId, String seats) {
        String endpoint = UriComponentsBuilder.fromHttpUrl(ROOT_URL + "/booking")
                .queryParam("userId", userId)
                .queryParam("eventId", eventId)
                .queryParam("seats", seats)
                .toUriString();

        ResponseEntity<Booking> bookingEntity = template.exchange(endpoint, HttpMethod.POST, null, Booking.class);
        Booking booking = bookingEntity.getBody();
        System.out.println("Booked:\n" + booking);
        return booking;
    }

    private static String listToString(List<?> list) {
        return list.stream().map(Object::toString).collect(Collectors.joining("\n"));
    }
}
