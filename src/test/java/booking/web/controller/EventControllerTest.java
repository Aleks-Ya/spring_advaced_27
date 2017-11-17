package booking.web.controller;

import booking.beans.configuration.db.DataSourceConfiguration;
import booking.beans.configuration.db.DbSessionFactory;
import booking.beans.services.EventService;
import booking.util.JsonUtil;
import booking.web.EnableWebMvcConfig;
import booking.web.FreeMarkerConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {EnableWebMvcConfig.class, FreeMarkerConfig.class, EventController.class,
        DataSourceConfiguration.class, DbSessionFactory.class,
        booking.beans.configuration.TestEventServiceConfiguration.class
})
public class EventControllerTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private EventService eventService;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test
    public void create() throws Exception {
        String name = "Discussion";

        assertThat(eventService.getByName(name), emptyIterable());

        String json = JsonUtil.format("{" +
                "'id': 1," +
                "'name': '%s'," +
                "'rate': 'HIGH'," +
                "'basePrice': 100.5," +
                "'dateTime': '2007-12-03T10:15:30'" +
                "}", name
        );
        mvc.perform(put("/event")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>Event created</h1>\n" +
                        "<p>Event{id=1, name='Discussion', rate=HIGH, basePrice=100.5, dateTime=2007-12-03T10:15:30, auditorium=null}</p>"));

        assertThat(eventService.getByName(name), hasSize(1));
    }

    @Test
    public void batchUpload() throws Exception {
        String name1 = "Hall";
        String name2 = "Room";

        assertThat(eventService.getByName(name1), emptyIterable());
        assertThat(eventService.getByName(name2), emptyIterable());

        String fileContent1 = JsonUtil.format("{" +
                "'id': 1," +
                "'name': '%s'," +
                "'rate': 'HIGH'," +
                "'basePrice': 100.5," +
                "'dateTime': '2007-12-03T10:15:30'" +
                "}", name1
        );
        MockMultipartFile multipartFile1 = new MockMultipartFile(EventController.PART_NAME, "filename1.json", MediaType.APPLICATION_JSON_VALUE, fileContent1.getBytes());

        String fileContent2 = JsonUtil.format("{" +
                "'id': 2," +
                "'name': '%s'," +
                "'rate': 'LOW'," +
                "'basePrice': 50," +
                "'dateTime': '2017-10-13T15:09:20'" +
                "}", name2
        );
        MockMultipartFile multipartFile2 = new MockMultipartFile(EventController.PART_NAME, "filename2.json", MediaType.APPLICATION_JSON_VALUE, fileContent2.getBytes());

        MockMultipartHttpServletRequestBuilder multipartBuilder = MockMvcRequestBuilders
                .fileUpload("/event/batchUpload")
                .file(multipartFile1)
                .file(multipartFile2);

        mvc.perform(multipartBuilder).andExpect(status().isOk());

        assertThat(eventService.getByName(name1), hasSize(1));
        assertThat(eventService.getByName(name2), hasSize(1));
    }
}