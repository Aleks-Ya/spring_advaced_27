package booking.web.pdf;


import booking.domain.Booking;
import booking.domain.Event;
import booking.domain.Ticket;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
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

/**
 * Convert body of type {@link BookingList} to PDF document.
 */
@Component
public class BookingPdfHttpMessageConverter implements HttpMessageConverter<BookingPdfHttpMessageConverter.BookingList> {
    private static final List<MediaType> supportedMediaTypes = Collections.singletonList(MediaType.APPLICATION_PDF);

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
            if (!bookings.isEmpty()) {
                for (Booking booking : bookings) {
                    Ticket ticket = booking.getTicket();
                    Event event = ticket.getEvent();
                    String eventStr = event != null ? event.getName() : "No event info";
                    Paragraph paragraph = new Paragraph(eventStr);
                    document.add(paragraph);
                }
            } else {
                document.add(new Paragraph(" "));
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
