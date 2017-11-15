package booking.web.controller;

import booking.beans.configuration.db.DataSourceConfiguration;
import booking.beans.configuration.db.DbSessionFactory;
import booking.beans.services.UserService;
import booking.util.JsonUtil;
import booking.web.FreeMarkerConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

    @Autowired
    private UserService userService;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void register() throws Exception {
        String body = JsonUtil.format("{" +
                "  'id': 1," +
                "  'name': 'John'," +
                "  'email': 'john@gmail.com'," +
                "  'birthday': '2000-07-03'" +
                "}");
        mvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>User is registered</h1>\n" +
                        "<p>Id: 1</p>\n" +
                        "<p>Name: John</p>\n" +
                        "<p>Email: john@gmail.com</p>\n" +
                        "<p>Birthday: 2000-07-03</p>"));
    }

    @Test
    public void getById() throws Exception {
        String body = JsonUtil.format("{" +
                "  'id': 2," +
                "  'name': 'Mary'," +
                "  'email': 'mary@gmail.com'," +
                "  'birthday': '2010-02-15'" +
                "}");
        registerUser(body);
        mvc.perform(get("/user/id/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>User</h1>\n" +
                        "<p>Id: 2</p>\n" +
                        "<p>Name: Mary</p>\n" +
                        "<p>Email: mary@gmail.com</p>\n" +
                        "<p>Birthday: 2010-02-15</p>"));
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

    @Test
    public void batchUpload() throws Exception {
        String email1 = "stenev@gmail.com";
        String email2 = "julia@gmail.com";

        assertNull(userService.getUserByEmail(email1));
        assertNull(userService.getUserByEmail(email2));

        String fileContent1 = JsonUtil.format("{" +
                "'id': 3," +
                "'name': 'Steven'," +
                "'email': 'stenev@gmail.com'," +
                "'birthday': '1990-07-03'" +
                "}"
        );
        MockMultipartFile multipartFile1 = new MockMultipartFile(UserController.PART_NAME, "filename1.json", MediaType.APPLICATION_JSON_VALUE, fileContent1.getBytes());

        String fileContent2 = JsonUtil.format("{" +
                "  'id': 4," +
                "  'name': 'Julia'," +
                "  'email': 'julia@gmail.com'," +
                "  'birthday': '2012-09-11'" +
                "}");
        MockMultipartFile multipartFile2 = new MockMultipartFile(UserController.PART_NAME, "filename2.json", MediaType.APPLICATION_JSON_VALUE, fileContent2.getBytes());

        MockMultipartHttpServletRequestBuilder multipartBuilder = MockMvcRequestBuilders
                .fileUpload("/user/batchUpload")
                .file(multipartFile1)
                .file(multipartFile2);

        mvc.perform(multipartBuilder).andExpect(status().isOk());

        assertNotNull(userService.getUserByEmail(email1));
        assertNotNull(userService.getUserByEmail(email2));
    }

    @EnableWebMvc
    @Configuration
    static class Config {
    }
}