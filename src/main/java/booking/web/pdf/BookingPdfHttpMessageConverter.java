package booking.web.pdf;


import booking.domain.Booking;
import booking.domain.Event;
import booking.domain.Ticket;
import booking.domain.User;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import static com.itextpdf.text.Font.FontFamily.UNDEFINED;
import static com.itextpdf.text.Font.FontStyle.BOLD;
import static java.lang.String.format;

/**
 * Convert body of type {@link BookingList} to PDF document.
 */
@Component
public class BookingPdfHttpMessageConverter implements HttpMessageConverter<BookingPdfHttpMessageConverter.BookingList> {
    private static final List<MediaType> supportedMediaTypes = Collections.singletonList(MediaType.APPLICATION_PDF);
    private static final Font HEADER_FONT = new Font(UNDEFINED, 36, BOLD.ordinal());

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return BookingList.class == clazz && MediaType.APPLICATION_PDF.equals(mediaType);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return supportedMediaTypes;
    }

    @Override
    public void write(BookingList bookingList, MediaType contentType, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        outputMessage.getHeaders().setContentType(MediaType.APPLICATION_PDF);
        bookingListToPdf(bookingList.getList(), outputMessage.getBody());
    }

    @Override
    public BookingList read(Class clazz, HttpInputMessage inputMessage) throws HttpMessageNotReadableException {
        throw new HttpMessageNotReadableException(clazz.getName());
    }


    private static void bookingListToPdf(List<Booking> bookings, OutputStream os) {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, os);
            writer.setViewerPreferences(PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage);
            document.open();
            document.add(new Paragraph("Bookings", HEADER_FONT));
            if (!bookings.isEmpty()) {
                for (Booking booking : bookings) {
                    Ticket ticket = booking.getTicket();
                    Event event = ticket.getEvent();
                    User user = booking.getUser();
                    String line = format("Id: %d, User: %s, Event: %s, Seats: %s, Price: %f",
                            booking.getId(), user.getName(), event.getName(), ticket.getSeats(), ticket.getPrice());
                    Paragraph paragraph = new Paragraph(line);
                    document.add(paragraph);
                }
            } else {
                document.add(new Paragraph("No bookings"));
            }
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Wrap List<Booking> due to canWrite() doesn't provide info about generics.
     */
    public static class BookingList {
        private final List<Booking> list;

        public BookingList(List<Booking> list) {
            this.list = list;
        }

        public List<Booking> getList() {
            return list;
        }
    }
}
