package booking.web.controller;

import booking.repository.config.DataSourceConfig;
import booking.repository.config.DbSessionFactoryConfig;
import booking.repository.config.TestUserServiceConfig;
import booking.service.UserService;
import booking.util.JsonUtil;
import booking.web.config.FreeMarkerConfig;
import booking.web.config.MvcConfig;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MvcConfig.class, FreeMarkerConfig.class, UserController.class,
        DataSourceConfig.class, DbSessionFactoryConfig.class, TestUserServiceConfig.class
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
    public void registerWithoutId() throws Exception {
        mvc.perform(post(UserController.ENDPOINT)
                .param("name", "John")
                .param("email", "john12@gmail.com")
                .param("birthday", "2000-07-03")
                .param("password", "pass")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        ).andExpect(status().isCreated())
                .andExpect(content().string(matchesPattern(
                        "<h1>User is registered</h1>\n" +
                                "<p>Id: \\d+</p>\n" +
                                "<p>Name: John</p>\n" +
                                "<p>Email: john12@gmail.com</p>\n" +
                                "<p>Birthday: 2000-07-03</p>")));
    }

    @Test
    @Ignore("Fix it")
    public void registerWithId() throws Exception {
        String body = JsonUtil.format("{" +
                " 'id': 333," +
                "  'name': 'John'," +
                "  'email': 'john1@gmail.com'," +
                "  'birthday': '2000-07-03'," +
                "  'password': 'pass'" +
                "}");
        ResultActions result = mvc.perform(post(UserController.ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        );
        result.andExpect(status().isCreated())
                .andExpect(content().string(
                        "<h1>User is registered</h1>\n" +
                                "<p>Id: 333</p>\n" +
                                "<p>Name: John</p>\n" +
                                "<p>Email: john1@gmail.com</p>\n" +
                                "<p>Birthday: 2000-07-03</p>"));
    }

    @Test
    @Ignore("Fix after implementing delete user method")
    public void getById() throws Exception {
        String body = JsonUtil.format("{" +
                " 'id': 123, " +
                "  'name': 'Mary'," +
                "  'email': 'mary3@gmail.com'," +
                "  'birthday': '2010-02-15'," +
                "  'password': 'pass'" +
                "}");
        registerUser(body);
        mvc.perform(get(UserController.ENDPOINT + "/id/123"))
                .andExpect(status().isOk())
                .andExpect(content().string(matchesPattern(
                        "<h1>User</h1>\n" +
                                "<p>Id: \\d+</p>\n" +
                                "<p>Name: Mary</p>\n" +
                                "<p>Email: mary3@gmail.com</p>\n" +
                                "<p>Birthday: 2010-02-15</p>")));
    }

    private void registerUser(String body) throws Exception {
        mvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isCreated());
    }

    @Test
    public void getByIdNotFound() throws Exception {
        mvc.perform(get(UserController.ENDPOINT + "/id/333"))
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
                "'name': 'Steven'," +
                "'email': '%s'," +
                "'birthday': '1990-07-03'," +
                "'password': 'pass'" +
                "}", email1
        );
        MockMultipartFile multipartFile1 = new MockMultipartFile(UserController.PART_NAME, "filename1.json", MediaType.APPLICATION_JSON_VALUE, fileContent1.getBytes());

        String fileContent2 = JsonUtil.format("{" +
                "  'name': 'Julia'," +
                "  'email': '%s'," +
                "  'birthday': '2012-09-11'," +
                "  'password': 'pass'" +
                "}", email2
        );
        MockMultipartFile multipartFile2 = new MockMultipartFile(UserController.PART_NAME, "filename2.json", MediaType.APPLICATION_JSON_VALUE, fileContent2.getBytes());

        MockMultipartHttpServletRequestBuilder multipartBuilder = MockMvcRequestBuilders
                .fileUpload(UserController.ENDPOINT + "/batchUpload")
                .file(multipartFile1)
                .file(multipartFile2);

        mvc.perform(multipartBuilder).andExpect(status().isOk());

        assertNotNull(userService.getUserByEmail(email1));
        assertNotNull(userService.getUserByEmail(email2));
    }
}