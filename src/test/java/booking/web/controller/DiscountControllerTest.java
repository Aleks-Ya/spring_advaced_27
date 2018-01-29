package booking.web.controller;

import booking.domain.Event;
import booking.domain.Rate;
import booking.domain.User;
import booking.repository.config.*;
import booking.service.EventService;
import booking.service.UserService;
import booking.service.impl.discount.DiscountServiceImpl;
import booking.web.config.FreeMarkerConfig;
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

import static booking.web.controller.LoginControllerTest.ANONYMOUS_HEADER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {FreeMarkerConfig.class, DiscountController.class, DiscountServiceImpl.class,
        DataSourceConfig.class, DbSessionFactoryConfig.class, TestStrategiesConfig.class,
        TestUserServiceConfig.class, TestEventServiceConfig.class
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
        User user = userService.register(new User("john2@gmail.com", "Test User", null, "pass", null));
        Event event = eventService.create(new Event("Meeting", Rate.HIGH, 100, null, null));
        mvc.perform(get(DiscountController.ENDPOINT)
                .param("userId", String.valueOf(user.getId()))
                .param("eventId", String.valueOf(event.getId()))
        )
                .andExpect(status().isOk())
                .andExpect(content().string(ANONYMOUS_HEADER + "<h1>Discount</h1>\n<p>0.5</p>"));
    }

    @Test
    public void getZeroDiscount() throws Exception {
        User user = userService.register(new User("john3@gmail.com", "John", null, "pass", null));
        Event event = eventService.create(new Event("Meeting2", Rate.HIGH, 100, null, null));
        mvc.perform(get(DiscountController.ENDPOINT)
                .param("userId", String.valueOf(user.getId()))
                .param("eventId", String.valueOf(event.getId()))
        )
                .andExpect(status().isOk())
                .andExpect(content().string(ANONYMOUS_HEADER + "<h1>Discount</h1>\n<p>0</p>"));
    }
}