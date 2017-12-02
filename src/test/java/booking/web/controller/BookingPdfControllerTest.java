package booking.web.controller;

import booking.beans.configuration.TestBookingServiceConfiguration;
import booking.beans.configuration.db.DataSourceConfiguration;
import booking.beans.configuration.db.DbSessionFactoryConfig;
import booking.beans.daos.mocks.BookingDAOBookingMock;
import booking.beans.daos.mocks.DBAuditoriumDAOMock;
import booking.beans.daos.mocks.EventDAOMock;
import booking.beans.daos.mocks.UserDAOMock;
import com.codeborne.pdftest.PDF;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.codeborne.pdftest.PDF.containsText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {BookingPdfController.class, PdfView.class,
        DbSessionFactoryConfig.class, DataSourceConfiguration.class, TestBookingServiceConfiguration.class})
@Transactional
public class BookingPdfControllerTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private BookingDAOBookingMock bookingDAOBookingMock;
    @Autowired
    private EventDAOMock eventDAOMock;
    @Autowired
    private UserDAOMock userDAOMock;
    @Autowired
    private DBAuditoriumDAOMock auditoriumDAOMock;

    private MockMvc mvc;

    @Before
    public void setup() {
        auditoriumDAOMock.init();
        userDAOMock.init();
        eventDAOMock.init();
        bookingDAOBookingMock.init();
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void requestParam() throws Exception {
        MvcResult mvcResult = mvc.perform(get(BookingPdfController.ENDPOINT).accept(MediaType.APPLICATION_PDF))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF_VALUE))
                .andReturn();

        byte[] body = mvcResult.getResponse().getContentAsByteArray();
        PDF bodyPdf = new PDF(body);
        assertThat(bodyPdf, allOf(
                containsText("Test event, 2016-02-06T14:45"),
                containsText("Test event2, 2016-02-07T14:45")
        ));
    }
}