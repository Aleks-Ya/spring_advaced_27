package booking.web.rest;

import booking.service.BookingService;
import booking.web.pdf.PdfView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@SuppressWarnings("unused")
class BookingPdfRestController {
    static final String ENDPOINT = BookingRestController.ENDPOINT;

    private final BookingService bookingService;

    @Autowired
    public BookingPdfRestController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @RequestMapping(path = BookingPdfRestController.ENDPOINT, produces = MediaType.APPLICATION_PDF_VALUE)
    ModelAndView getPdf() {
        Map<String, Object> model = new HashMap<>();
        model.put(PdfView.BOOKINGS_KEY, bookingService.getAll());
        return new ModelAndView(new PdfView(), model);
    }

}
