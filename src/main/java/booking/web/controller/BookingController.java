package booking.web.controller;

import booking.domain.models.Event;
import booking.domain.models.Ticket;
import booking.domain.models.User;
import booking.service.BookingService;
import booking.service.EventService;
import booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Aleksey Yablokov
 */
@Controller
@SuppressWarnings("unused")
@RequestMapping(BookingController.ENDPOINT)
public class BookingController {
    static final String ENDPOINT = "/booking";
    private static final String TICKETS_ATTR = "tickets";
    private static final String TICKET_ATTR = "ticket";
    private static final String EVENTS_ATTR = "events";
    private static final String TICKET_PRICE_ATTR = "price";
    private static final String TICKETS_FTL = "booking/booked_tickets";
    private static final String TICKET_FTL = "booking/ticket";
    private static final String TICKET_PRICE_FTL = "booking/ticket_price";
    private static final String TICKET_FOR_EVENT_FTL = "booking/ticket_for_event";
    private static final String BOOKED_TICKET_FTL = "booking/booked_ticket";

    private final BookingService bookingService;
    private final UserService userService;
    private final EventService eventService;

    @Autowired
    public BookingController(BookingService bookingService, UserService userService, EventService eventService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.eventService = eventService;
    }

    @RequestMapping(method = RequestMethod.GET)
    String getBookedTickets(@ModelAttribute("model") ModelMap model) {
        model.addAttribute(TICKETS_ATTR, bookingService.getBookedTickets());
        return TICKETS_FTL;
    }

    @RequestMapping(path = "/price", method = RequestMethod.GET)
    String getTicketPrice(
            @RequestParam String eventName,
            @RequestParam String auditoriumName,
            @RequestParam Long userId,
            @RequestParam String localDateTime,
            @RequestParam String seats,
            @ModelAttribute("model") ModelMap model) {
        User user = userService.getById(userId);
        LocalDateTime date = LocalDateTime.parse(localDateTime);
        List<Integer> seatsList = SeatHelper.parseSeatsString(seats);
        double price = bookingService.getTicketPrice(eventName, String.valueOf(auditoriumName), date, seatsList, user);
        model.addAttribute(TICKET_PRICE_ATTR, price);
        return TICKET_PRICE_FTL;
    }

    @RequestMapping(path = "/tickets", method = RequestMethod.GET)
    String getTicketsForEvent(
            @RequestParam String eventName,
            @RequestParam Long auditoriumId,
            @RequestParam String localDateTime,
            @ModelAttribute("model") ModelMap model) {
        LocalDateTime date = LocalDateTime.parse(localDateTime);
        List<Ticket> tickets = bookingService.getTicketsForEvent(eventName, auditoriumId, date);
        List<Event> events = eventService.getByName(eventName);
        model.addAttribute(TICKETS_ATTR, tickets);
        model.addAttribute(EVENTS_ATTR, events);
        return TICKET_FOR_EVENT_FTL;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    String bookTicket(@RequestParam Long userId,
                      @RequestParam Long eventId,
                      @RequestParam String localDateTime,
                      @RequestParam String seats,
                      @RequestParam Double price,
                      @ModelAttribute("model") ModelMap model) {
        User user = userService.getById(userId);
        Event event = eventService.getById(eventId);
        List<Integer> seatsList = Stream.of(seats.split(",")).map(Integer::valueOf).collect(Collectors.toList());
        LocalDateTime date = LocalDateTime.parse(localDateTime);
        Ticket ticket = new Ticket(event, date, seatsList, user, price);

        Ticket bookedTicket = bookingService.bookTicket(user, ticket);

        model.addAttribute(TICKET_ATTR, bookedTicket);
        return BOOKED_TICKET_FTL;
    }

    @RequestMapping(path = "/id/{ticketId}", method = RequestMethod.GET)
    String getTicketById(@PathVariable Long ticketId, @ModelAttribute("model") ModelMap model) {
        Ticket ticket = bookingService.getTicketById(ticketId);
        model.addAttribute(TICKET_ATTR, ticket);
        return TICKET_FTL;
    }
}
