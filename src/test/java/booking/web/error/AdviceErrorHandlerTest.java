package booking.web.error;

import booking.repository.config.DataSourceConfig;
import booking.repository.config.DbSessionFactoryConfig;
import booking.web.config.FreeMarkerConfig;
import booking.web.config.MvcConfig;
import booking.web.controller.UserController;
import booking.web.security.SecurityConfig;
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
@ContextConfiguration(classes = {MvcConfig.class, FreeMarkerConfig.class, UserController.class, DataSourceConfig.class,
        DbSessionFactoryConfig.class, booking.repository.config.TestUserServiceConfig.class, AdviceErrorHandler.class,
        SecurityConfig.class
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
        mvc.perform(post(UserController.ENDPOINT)
                .param("name", "John")
                .param("birthday", "2000-07-03")
                .param("password", "pass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        )
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>An error occurred</h1>\n" +
                        "<p>User's email is [null]: [User {email='null', name='John', birthday='2000-07-03'}]</p>"));
    }

}