package booking.web.controller;

import booking.beans.configuration.db.DataSourceConfiguration;
import booking.beans.configuration.db.DbSessionFactory;
import booking.web.FreeMarkerConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {FreeMarkerConfig.class, AuditoriumController.class, DataSourceConfiguration.class,
        DbSessionFactory.class, booking.beans.configuration.TestAuditoriumConfiguration.class
})
public class AuditoriumControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void auditoriumsGet() throws Exception {
        mvc.perform(get("/auditorium"))
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>Auditoriums</h1>\n" +
                        "<p>Auditorium</p>\n" +
                        "<p>Id: 1</p>\n" +
                        "<p>Name: Blue hall</p>\n" +
                        "<p>Seats number: 500</p>\n" +
                        "<p>VIP seats: 25,26,27,28,29,30,31,32,33,34,35</p><hr/>\n" +
                        "<p>Auditorium</p>\n" +
                        "<p>Id: 2</p>\n" +
                        "<p>Name: Red hall</p>\n" +
                        "<p>Seats number: 800</p>\n" +
                        "<p>VIP seats: 25,26,27,28,29,30,31,32,33,34,35,75,76,77,78,79,80,81,82,83,84,85</p><hr/>\n" +
                        "<p>Auditorium</p>\n" +
                        "<p>Id: 3</p>\n" +
                        "<p>Name: Yellow hall</p>\n" +
                        "<p>Seats number: 1,000</p>\n" +
                        "<p>VIP seats: 25,26,27,28,29,30,31,32,33,34,35,75,76,77,78,79,80,81,82,83,84,85,105,106,107,108,109,110,111,112,113,114,115</p><hr/>\n"));
    }

    @Test
    public void auditoriumsPost() throws Exception {
        mvc.perform(post("/auditorium")).andExpect(status().is(HttpStatus.METHOD_NOT_ALLOWED.value()));
    }

    @Test
    public void auditoriumByIdGet() throws Exception {
        mvc.perform(get("/auditorium/id/123")).andExpect(status().is(HttpStatus.METHOD_NOT_ALLOWED.value()));
    }

    @Test
    public void auditoriumByNameGet() throws Exception {
        mvc.perform(get("/auditorium/name/Blue hall"))
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>Auditorium</h1>\n" +
                        "<p>Id: 1</p>\n" +
                        "<p>Name: Blue hall</p>\n" +
                        "<p>Seats number: 500</p>\n" +
                        "<p>VIP seats: 25,26,27,28,29,30,31,32,33,34,35</p>"));
    }

    @Test
    public void seatsNumberByAuditoriumNameGet() throws Exception {
        mvc.perform(get("/auditorium/name/Blue hall/seatsNumber"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "<h1>Seats number</h1>\n" +
                                "<p>Auditorium: Blue hall</p>\n" +
                                "<p>Seats number: 500</p>"));
    }

    @Test
    public void seatsNumberByAuditoriumIdGet() throws Exception {
        mvc.perform(get("/auditorium/id/123/seatsNumber"))
                .andExpect(status().is(HttpStatus.METHOD_NOT_ALLOWED.value()));
    }

    @Test
    public void vipSeatsByAuditoriumNameGet() throws Exception {
        mvc.perform(get("/auditorium/name/Blue hall/vipSeats"))
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>VIP seats</h1>\n" +
                        "<p>Auditorium: Blue hall</p>\n" +
                        "<p>VIP seats: 25,26,27,28,29,30,31,32,33,34,35</p>"));
    }

    @Test
    public void vipSeatsByAuditoriumIdGet() throws Exception {
        mvc.perform(get("/auditorium/id/123/vipSeats"))
                .andExpect(status().is(HttpStatus.METHOD_NOT_ALLOWED.value()));
    }
}