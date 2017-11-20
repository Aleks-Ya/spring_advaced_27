package booking.web.controller;

import booking.beans.configuration.TestAuditoriumConfiguration;
import booking.beans.configuration.db.DataSourceConfiguration;
import booking.beans.configuration.db.DbSessionFactory;
import booking.beans.models.Auditorium;
import booking.beans.services.AuditoriumService;
import booking.web.configuration.FreeMarkerConfig;
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

import java.util.Arrays;

import static booking.web.controller.AuditoriumController.ENDPOINT;
import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {FreeMarkerConfig.class, AuditoriumController.class, DataSourceConfiguration.class,
        DbSessionFactory.class, TestAuditoriumConfiguration.class
})
public class AuditoriumControllerTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private AuditoriumService auditoriumService;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void createAuditorium() throws Exception {
        String auditoriumName = "Room";
        int auditoriumId = auditoriumService.getAuditoriums().size() + 1;
        mvc.perform(put(ENDPOINT)
                .param("auditoriumName", auditoriumName)
                .param("seatsNumber", String.valueOf(1000))
                .param("vipSeats", "1,2,3,4")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(format("<h1>Auditorium is created</h1>\n" +
                                "<p>Id: %d</p>\n" +
                                "<p>Name: Room</p>\n" +
                                "<p>Seats number: 1,000</p>\n" +
                                "<p>VIP seats: 1,2,3,4</p>",
                        auditoriumId
                )));
    }

    @Test
    public void getAuditoriums() throws Exception {
        deleteAllAuditoriums();
        Auditorium auditorium = auditoriumService.create(new Auditorium("Meeting room", 500, Arrays.asList(1, 2, 3)));
        mvc.perform(get("/auditorium"))
                .andExpect(status().isOk())
                .andExpect(content().string(format("<h1>Auditoriums</h1>\n" +
                                "<p>Auditorium</p>\n" +
                                "<p>Id: %d</p>\n" +
                                "<p>Name: Meeting room</p>\n" +
                                "<p>Seats number: 500</p>\n" +
                                "<p>VIP seats: 1,2,3</p><hr/>\n",
                        auditorium.getId())));
    }

    private void deleteAllAuditoriums() {
        auditoriumService.getAuditoriums().forEach(auditorium -> auditoriumService.delete(auditorium.getId()));
    }

    @Test
    public void auditoriumsPost() throws Exception {
        mvc.perform(post("/auditorium")).andExpect(status().is(HttpStatus.METHOD_NOT_ALLOWED.value()));
    }

    @Test
    public void getById() throws Exception {
        Auditorium auditorium = auditoriumService.create(new Auditorium("Meeting room", 500, Arrays.asList(1, 2, 3)));
        mvc.perform(get("/auditorium/id/" + auditorium.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(format("<h1>Auditorium</h1>\n" +
                        "<p>Id: %d</p>\n" +
                        "<p>Name: Meeting room</p>\n" +
                        "<p>Seats number: 500</p>\n" +
                        "<p>VIP seats: 1,2,3</p>",
                        auditorium.getId())));
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
        deleteAllAuditoriums();
        String auditoriumName = "Blue hall";
        auditoriumService.create(new Auditorium(auditoriumName, 500, Arrays.asList(1, 2, 3)));
        mvc.perform(get(format("/auditorium/name/%s/seatsNumber", auditoriumName)))
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