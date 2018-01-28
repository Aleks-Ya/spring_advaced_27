package booking.web.controller;

import booking.domain.Auditorium;
import booking.repository.config.DataSourceConfig;
import booking.repository.config.DbSessionFactoryConfig;
import booking.repository.config.TestAuditoriumConfig;
import booking.repository.config.TestUserServiceConfig;
import booking.service.AuditoriumService;
import booking.web.config.FreeMarkerConfig;
import booking.web.config.MvcConfig;
import booking.web.error.AdviceErrorHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static booking.web.controller.AuditoriumController.ENDPOINT;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {FreeMarkerConfig.class, AuditoriumController.class, DataSourceConfig.class,
        DbSessionFactoryConfig.class, TestAuditoriumConfig.class, AdviceErrorHandler.class, MvcConfig.class,
        TestUserServiceConfig.class
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
        String expAuditoriumName = "Room";
        String expSeatsNumber = String.valueOf(1000);
        String expVipSeats = "1,2,3,4";

        ResultActions resultActions = mvc.perform(post(ENDPOINT)
                .param("auditoriumName", expAuditoriumName)
                .param("seatsNumber", expSeatsNumber)
                .param("vipSeats", expVipSeats)
        );

        MvcResult mvcResult = resultActions.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Matcher m = Pattern.compile("Id: (\\d+)").matcher(content);
        assertTrue(m.find());
        long auditoriumId = Long.parseLong(m.group(1));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().string(format("<h1>Auditorium is created</h1>\n" +
                                "<p>Id: %d</p>\n" +
                                "<p>Name: Room</p>\n" +
                                "<p>Seats number: 1,000</p>\n" +
                                "<p>VIP seats: 1,2,3,4</p>",
                        auditoriumId
                )));

        Auditorium actAuditorium = auditoriumService.getById(auditoriumId);
        assertThat(actAuditorium.getName(), equalTo(expAuditoriumName));
        assertThat(Integer.toString(actAuditorium.getSeatsNumber()), equalTo(expSeatsNumber));
        assertThat(actAuditorium.getVipSeats(), equalTo(expVipSeats));

        auditoriumService.delete(auditoriumId);
    }

    @Test
    public void getAuditoriums() throws Exception {
        mvc.perform(get("/auditorium"))
                .andExpect(status().isOk())
                .andExpect(content().string("User: Anonymous\n" +
                        "<h1>Auditoriums</h1>\n" +
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
        auditoriumService.delete(auditorium.getId());
    }

    @Test
    public void getAuditoriumByName() throws Exception {
        Auditorium auditorium = auditoriumService.create(new Auditorium("Relax room", 500, Arrays.asList(1, 2, 3)));
        mvc.perform(get("/auditorium/name/" + auditorium.getName()))
                .andExpect(status().isOk())
                .andExpect(content().string(format("<h1>Auditorium</h1>\n" +
                                "<p>Id: %d</p>\n" +
                                "<p>Name: Relax room</p>\n" +
                                "<p>Seats number: 500</p>\n" +
                                "<p>VIP seats: 1,2,3</p>",
                        auditorium.getId())));
    }

    @Test
    public void seatsNumberByAuditoriumNameGet() throws Exception {
        String auditoriumName = UUID.randomUUID().toString();
        Auditorium auditorium = auditoriumService.create(new Auditorium(auditoriumName, 500, Arrays.asList(1, 2, 3)));
        mvc.perform(get(format("/auditorium/name/%s/seatsNumber", auditoriumName)))
                .andExpect(status().isOk())
                .andExpect(content().string(format(
                        "<h1>Seats number</h1>\n" +
                                "<p>Auditorium: %s</p>\n" +
                                "<p>Seats number: 500</p>", auditoriumName)));
        auditoriumService.delete(auditorium.getId());
    }

    @Test
    public void seatsNumberByAuditoriumIdGet() throws Exception {
        String auditoriumName = UUID.randomUUID().toString();
        Auditorium auditorium = auditoriumService.create(new Auditorium(auditoriumName, 500, Arrays.asList(1, 2, 3)));
        mvc.perform(get(format("/auditorium/id/%s/seatsNumber", auditorium.getId())))
                .andExpect(status().isOk())
                .andExpect(content().string(format(
                        "<h1>Seats number</h1>\n" +
                                "<p>Auditorium: %s</p>\n" +
                                "<p>Seats number: 500</p>", auditoriumName)));
        auditoriumService.delete(auditorium.getId());
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
        Auditorium auditorium = auditoriumService.create(new Auditorium("Red room", 500, Arrays.asList(1, 2, 3)));
        mvc.perform(get(format("/auditorium/id/%d/vipSeats", auditorium.getId())))
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>VIP seats</h1>\n" +
                        "<p>Auditorium: Red room</p>\n" +
                        "<p>VIP seats: 1,2,3</p>"));
    }

    @Test
    public void deleteExistsAuditorium() throws Exception {
        String auditoriumName = UUID.randomUUID().toString();
        Auditorium auditorium = auditoriumService.create(new Auditorium(auditoriumName, 500, Arrays.asList(1, 2, 3)));
        mvc.perform(delete(format("/auditorium/id/%s/delete", auditorium.getId())))
                .andExpect(status().isOk())
                .andExpect(content().string(format(
                        "<h1>Auditorium is deleted</h1>\n" +
                                "<p>Id: %d</p>\n" +
                                "<p>Name: %s</p>\n" +
                                "<p>Seats number: 500</p>\n" +
                                "<p>VIP seats: 1,2,3</p>",
                        auditorium.getId(), auditoriumName)));
    }

    @Test
    public void deleteNotExistsAuditorium() throws Exception {
        int notExistsAuditoriumId = 1234567;
        mvc.perform(delete(format("/auditorium/id/%s/delete", notExistsAuditoriumId)))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "<h1>An error occurred</h1>\n" +
                                "<p>Auditorium is not found by id=1234567</p>"));
    }
}