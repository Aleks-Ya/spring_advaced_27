package booking.web.rest;

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

@ContextConfiguration(classes = {BookingPdfRestController.class})
public class BookingPdfRestControllerTest extends BaseWebTest {

    @Test
    public void requestParam() throws Exception {
        to.bookTicketToParty();
        to.bookTicketToHackathon();
        MvcResult mvcResult = mvc.perform(get(BookingPdfRestController.ENDPOINT).accept(MediaType.APPLICATION_PDF))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF_VALUE))
                .andReturn();

        byte[] body = mvcResult.getResponse().getContentAsByteArray();
        PDF bodyPdf = new PDF(body);
        assertThat(bodyPdf, allOf(
                containsText("New Year Party"),
                containsText("Java Hackathon")
        ));
    }
}