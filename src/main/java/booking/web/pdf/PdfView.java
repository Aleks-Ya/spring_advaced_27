package booking.web.pdf;


import booking.domain.Booking;
import booking.domain.Event;
import booking.domain.Ticket;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Make a PDF document contains booking passed in BOOKINGS_KEY entry of the model.
 */
public class PdfView extends AbstractView {

    public static final String BOOKINGS_KEY = "bookings";

    public PdfView() {
        setContentType(MediaType.APPLICATION_PDF_VALUE);
    }

    private void buildPdfDocument(Map<String, Object> model, Document document) throws DocumentException {
        @SuppressWarnings("unchecked")
        List<Booking> bookings = (List<Booking>) model.get(BOOKINGS_KEY);

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
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws DocumentException, IOException {

        ByteArrayOutputStream out = createTemporaryOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, out);
        writer.setViewerPreferences(PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage);

        document.open();
        buildPdfDocument(model, document);
        document.close();
        writeToResponse(response, out);
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }
}
