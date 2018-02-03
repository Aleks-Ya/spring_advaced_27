package booking.web.controller;

import booking.BaseWebTest;
import com.codeborne.pdftest.PDF;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import static com.codeborne.pdftest.PDF.containsText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@ContextConfiguration(classes = {BookingPdfController.class, PdfView.class})
public class BookingPdfControllerTest extends BaseWebTest {

    @Test
    public void requestParam() throws Exception {
        testObjects.bookTicketToParty();
        testObjects.bookTicketToHackathon();
        MvcResult mvcResult = mvc.perform(get(BookingPdfController.ENDPOINT).accept(MediaType.APPLICATION_PDF))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF_VALUE))
                .andReturn();

        byte[] body = mvcResult.getResponse().getContentAsByteArray();
        PDF bodyPdf = new PDF(body);
        assertThat(bodyPdf, allOf(
                containsText("New Year Party, 2018-12-31T23:00"),
                containsText("Java Hackathon, 2018-03-13T09:00")
        ));
    }
}