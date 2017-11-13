package booking.web.controller;

import booking.beans.configuration.db.DataSourceConfiguration;
import booking.beans.configuration.db.DbSessionFactory;
import booking.util.ResourceUtil;
import booking.web.FreeMarkerConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {UserControllerTest.Config.class, FreeMarkerConfig.class, UserController.class, DataSourceConfiguration.class,
        DbSessionFactory.class, booking.beans.configuration.TestUserServiceConfiguration.class
})
public class UserControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void register() throws Exception {
        String body = ResourceUtil.resourceToString("UserControllerTest_register.json", UserControllerTest.class);
        mvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>User is registered</h1>\n" +
                        "<p>Id: 1</p>\n" +
                        "<p>Name: John</p>\n" +
                        "<p>Email: john@gmail.com</p>\n" +
                        "<p>Birthday: no value</p>"));
    }

    @Test
    public void getById() throws Exception {
        String body = ResourceUtil.resourceToString("UserControllerTest_getById.json", UserControllerTest.class);
        registerUser(body);
        mvc.perform(get("/user/id/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>User</h1>\n" +
                        "<p>Id: 2</p>\n" +
                        "<p>Name: Mary</p>\n" +
                        "<p>Email: mary@gmail.com</p>\n" +
                        "<p>Birthday: no value</p>"));
    }

    private void registerUser(String body) throws Exception {
        mvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isOk());
    }

    @Test
    public void getByIdNotFound() throws Exception {
        mvc.perform(get("/user/id/333"))
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>User</h1>\n" +
                        "No user info"));
    }

    @EnableWebMvc
    @Configuration
    static class Config {
    }
}