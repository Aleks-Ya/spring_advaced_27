package booking.web.controller;

import booking.beans.models.Event;
import booking.beans.models.Ticket;
import booking.beans.models.User;
import booking.beans.services.BookingService;
import booking.beans.services.EventService;
import booking.beans.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    private static final String TICKETS_FTL = "booking/booked_tickets";
    private static final String TICKET_FTL = "booking/ticket";
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

    @RequestMapping(method = RequestMethod.PUT)
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

    @RequestMapping(path = "/{ticketId}", method = RequestMethod.GET)
    String getTicketById(@PathVariable Long ticketId, @ModelAttribute("model") ModelMap model) {
        Ticket ticket = bookingService.getTicketById(ticketId);
        model.addAttribute(TICKET_ATTR, ticket);
        return TICKET_FTL;
    }
}
