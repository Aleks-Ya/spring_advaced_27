package booking.web.controller;

import booking.domain.Booking;
import booking.domain.Ticket;
import booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@SuppressWarnings("unused")
class BookingPdfController {
    static final String ENDPOINT = BookingController.ROOT_ENDPOINT + "/bookedTickets";
    static final String TICKETS_KEY = "tickets";

    private final BookingService bookingService;

    @Autowired
    public BookingPdfController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @RequestMapping(path = BookingPdfController.ENDPOINT, produces = MediaType.APPLICATION_PDF_VALUE)
    ModelAndView getPdf() {
        List<Ticket> tickets = bookingService.getAll().stream()
                .map(Booking::getTicket)
                .collect(Collectors.toList());
        Map<String, Object> model = new HashMap<>();
        model.put(TICKETS_KEY, tickets);
        return new ModelAndView(new PdfView(), model);
    }

}
