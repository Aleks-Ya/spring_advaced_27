package booking.web.rest;

import booking.service.BookingService;
import booking.web.controller.BookingController;
import booking.web.pdf.BookingPdfHttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@SuppressWarnings("unused")
class BookingPdfRestController {
    static final String ENDPOINT = BookingRestController.ENDPOINT;

    private final BookingService bookingService;

    @Autowired
    public BookingPdfRestController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @RequestMapping(path = {BookingPdfRestController.ENDPOINT, BookingController.ROOT_ENDPOINT + "/bookedTickets"},
            method = GET, produces = APPLICATION_PDF_VALUE)
    BookingPdfHttpMessageConverter.BookingList getPdf() {
        return new BookingPdfHttpMessageConverter.BookingList(bookingService.getAll());
    }

}
