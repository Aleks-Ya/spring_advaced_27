package booking.web.controller;

import booking.beans.models.Ticket;
import booking.beans.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
class BookingPdfController {
    static final String ENDPOINT = BookingController.ENDPOINT + "/bookedTickets";
    static final String TICKETS_KEY = "tickets";

    @Autowired
    private BookingService bookingService;

    @SuppressWarnings("unused")
    @RequestMapping(path = BookingPdfController.ENDPOINT, produces = MediaType.APPLICATION_PDF_VALUE)
    ModelAndView getPdf() {
        List<Ticket> tickets = bookingService.getBookedTickets();
        Map<String, Object> model = new HashMap<>();
        model.put(TICKETS_KEY, tickets);
        return new ModelAndView(new PdfView(), model);
    }

}
