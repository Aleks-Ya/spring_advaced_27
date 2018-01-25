package booking.web.controller;


import booking.domain.Event;
import booking.domain.Ticket;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

class PdfView extends AbstractView {

    PdfView() {
        setContentType("application/pdf");
    }

    private void buildPdfDocument(Map<String, Object> model, Document document) throws DocumentException {
        @SuppressWarnings("unchecked")
        List<Ticket> tickets = (List<Ticket>) model.get(BookingPdfController.TICKETS_KEY);

        if (!tickets.isEmpty()) {
            for (Ticket ticket : tickets) {
                Event event = ticket.getEvent();
                String eventStr = event != null ? event.getName() : "No event info";
                String ticketLine = String.format("%s, %s", eventStr, ticket.getDateTime());
                Paragraph paragraph = new Paragraph(ticketLine);
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
