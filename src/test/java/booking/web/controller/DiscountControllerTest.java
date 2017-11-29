package booking.web.controller;

import booking.beans.configuration.StrategiesConfiguration;
import booking.beans.configuration.TestEventServiceConfiguration;
import booking.beans.configuration.TestStrategiesConfiguration;
import booking.beans.configuration.TestUserServiceConfiguration;
import booking.beans.configuration.db.DataSourceConfiguration;
import booking.beans.configuration.db.DbSessionFactory;
import booking.beans.models.Event;
import booking.beans.models.Rate;
import booking.beans.models.User;
import booking.beans.services.DiscountServiceImpl;
import booking.beans.services.EventService;
import booking.beans.services.UserService;
import booking.web.configuration.FreeMarkerConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {FreeMarkerConfig.class, DiscountController.class, DiscountServiceImpl.class,
        DataSourceConfiguration.class, DbSessionFactory.class,
        TestStrategiesConfiguration.class, StrategiesConfiguration.class, TestUserServiceConfiguration.class,
        TestEventServiceConfiguration.class
})
public class DiscountControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void getNonZeroDiscount() throws Exception {
        User user = userService.register(new User("john@gmail.com", "Test User", null, "pass", null));
        Event event = eventService.create(new Event("Meeting", Rate.HIGH, 100, null, null));
        mvc.perform(get(DiscountController.ENDPOINT)
                .param("userId", String.valueOf(user.getId()))
                .param("eventId", String.valueOf(event.getId()))
        )
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>Discount</h1>\n<p>0.5</p>"));
    }

    @Test
    public void getZeroDiscount() throws Exception {
        User user = userService.register(new User("john2@gmail.com", "John", null, "pass", null));
        Event event = eventService.create(new Event("Meeting2", Rate.HIGH, 100, null, null));
        mvc.perform(get(DiscountController.ENDPOINT)
                .param("userId", String.valueOf(user.getId()))
                .param("eventId", String.valueOf(event.getId()))
        )
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>Discount</h1>\n<p>0</p>"));
    }
}