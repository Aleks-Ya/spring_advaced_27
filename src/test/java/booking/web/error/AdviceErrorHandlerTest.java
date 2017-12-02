package booking.web.error;

import booking.beans.configuration.db.DataSourceConfiguration;
import booking.beans.configuration.db.DbSessionFactoryConfig;
import booking.util.JsonUtil;
import booking.web.configuration.FreeMarkerConfig;
import booking.web.configuration.MvcConfig;
import booking.web.controller.UserController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MvcConfig.class, FreeMarkerConfig.class, UserController.class, DataSourceConfiguration.class,
        DbSessionFactoryConfig.class, booking.beans.configuration.TestUserServiceConfiguration.class, AdviceErrorHandler.class
})
public class AdviceErrorHandlerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void register() throws Exception {
        String body = JsonUtil.format("{" +
                "  'name': 'John'," +
                "  'email': null," +
                "  'birthday': '2000-07-03'," +
                "  'password': 'pass'" +
                "}");
        mvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>An error occurred</h1>\n" +
                        "<p>User's email is [null]: [User {email='null', name='John', birthday='2000-07-03'}]</p>"));
    }

}