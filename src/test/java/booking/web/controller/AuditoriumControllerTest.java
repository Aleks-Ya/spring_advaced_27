package booking.web.controller;

import booking.BaseWebTest;
import booking.domain.Auditorium;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static booking.web.controller.AuditoriumController.ENDPOINT;
import static booking.web.controller.LoginControllerTest.ANONYMOUS_HEADER;
import static booking.web.controller.RootControllerTest.NAVIGATOR;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {AuditoriumController.class})
public class AuditoriumControllerTest extends BaseWebTest {

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
                .andExpect(content().string(format(ANONYMOUS_HEADER +
                                "<h1>Auditorium is created</h1>\n" +
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
    }

    @Test
    public void getAuditoriums() throws Exception {
        Auditorium blueHall = to.createBlueHall();
        Auditorium redHall = to.createRedHall();
        mvc.perform(get(AuditoriumController.ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().string(format(ANONYMOUS_HEADER +
                                NAVIGATOR +
                                "<h1>Auditoriums</h1>\n" +
                                "<p>Auditorium</p>\n" +
                                "<p>Id: %d</p>\n" +
                                "<p>Name: Blue hall</p>\n" +
                                "<p>Seats number: 1,000</p>\n" +
                                "<p>VIP seats: 1,2,3,4,5</p><hr/>\n" +
                                "<p>Auditorium</p>\n" +
                                "<p>Id: %d</p>\n" +
                                "<p>Name: Red hall</p>\n" +
                                "<p>Seats number: 500</p>\n" +
                                "<p>VIP seats: 1,2,3</p><hr/>\n",
                        blueHall.getId(),
                        redHall.getId())));
    }

    @Test
    public void getById() throws Exception {
        Auditorium auditorium = to.createRedHall();
        mvc.perform(get(AuditoriumController.ENDPOINT + "/" + auditorium.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(format(ANONYMOUS_HEADER +
                                "<h1>Auditorium</h1>\n" +
                                "<p>Id: %d</p>\n" +
                                "<p>Name: Red hall</p>\n" +
                                "<p>Seats number: 500</p>\n" +
                                "<p>VIP seats: 1,2,3</p>",
                        auditorium.getId())));
    }

    @Test
    public void seatsNumberByAuditoriumIdGet() throws Exception {
        Auditorium auditorium = to.createRedHall();
        mvc.perform(get(format(AuditoriumController.ENDPOINT + "/%s/seatsNumber", auditorium.getId())))
                .andExpect(status().isOk())
                .andExpect(content().string(format(ANONYMOUS_HEADER +
                                "<h1>Seats number</h1>\n" +
                                "<p>Auditorium: %s</p>\n" +
                                "<p>Seats number: %d</p>",
                        auditorium.getName(), auditorium.getSeatsNumber())));
    }

    @Test
    public void vipSeatsByAuditoriumIdGet() throws Exception {
        Auditorium auditorium = to.createRedHall();
        mvc.perform(get(format(AuditoriumController.ENDPOINT + "/%d/vipSeats", auditorium.getId())))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        ANONYMOUS_HEADER +
                                "<h1>VIP seats</h1>\n" +
                                "<p>Auditorium: Red hall</p>\n" +
                                "<p>VIP seats: 1,2,3</p>"));
    }

    @Test
    public void deleteExistsAuditorium() throws Exception {
        Auditorium auditorium = to.createRedHall();
        mvc.perform(delete(format(AuditoriumController.ENDPOINT + "/%s/delete", auditorium.getId())))
                .andExpect(status().isOk())
                .andExpect(content().string(format(ANONYMOUS_HEADER +
                                "<h1>Auditorium is deleted</h1>\n" +
                                "<p>Id: %d</p>\n" +
                                "<p>Name: %s</p>\n" +
                                "<p>Seats number: %d</p>\n" +
                                "<p>VIP seats: 1,2,3</p>",
                        auditorium.getId(), auditorium.getName(), auditorium.getSeatsNumber())));
    }

    @Test
    public void deleteNotExistsAuditorium() throws Exception {
        int notExistsAuditoriumId = 1234567;
        mvc.perform(delete(format(AuditoriumController.ENDPOINT + "/%s/delete", notExistsAuditoriumId)))
                .andExpect(status().isOk())
                .andExpect(content().string(ANONYMOUS_HEADER + NAVIGATOR +
                        "<h1>Auditorium is not found</h1>\n" +
                        "<p>Auditorium is not found by id 1234567</p>"));
    }
}