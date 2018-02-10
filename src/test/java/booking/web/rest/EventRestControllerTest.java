package booking.web.rest;

import booking.BaseWebTest;
import booking.domain.Event;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = EventRestController.class)
public class EventRestControllerTest extends BaseWebTest {

    @Test
    public void getById() throws Exception {
        Event expEvent = to.createParty();
        MvcResult mvcResult = mvc.perform(get(EventRestController.ENDPOINT + "/" + expEvent.getId()))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Event actEvent = objectMapper.readValue(content, Event.class);

        assertThat(actEvent, equalTo(expEvent));
    }

    @Test
    public void getAll() throws Exception {
        Event expEvent1 = to.createParty();
        Event expEvent2 = to.createHackathon();
        MvcResult mvcResult = mvc.perform(get(EventRestController.ENDPOINT))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        List<Event> actEvents = objectMapper.readValue(content, new TypeReference<List<Event>>() {
        });

        assertThat(actEvents, containsInAnyOrder(expEvent1, expEvent2));
    }
}