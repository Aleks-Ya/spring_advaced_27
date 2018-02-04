package booking.web.controller;

import booking.BaseWebTest;
import booking.domain.User;
import booking.util.JsonUtil;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

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
@ContextConfiguration(classes = UserController.class)
public class UserControllerTest extends BaseWebTest {

    @Test
    public void register() throws Exception {
        String expEmail = "john12@gmail.com";
        String expName = "John";
        LocalDate expBirthday = LocalDate.parse("2000-07-03");
        String rawPassword = "pass";

        MockHttpSession session = new MockHttpSession();
        mvc.perform(post(UserController.REGISTER_ENDPOINT).session(session)
                .param("name", expName)
                .param("email", expEmail)
                .param("birthday", expBirthday.toString())
                .param("password", rawPassword)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        ).andExpect(status().isCreated())
                .andExpect(content().string(matchesPattern(
                        ".+\n" +
                                "<h1>User is registered</h1>\n" +
                                "<p>Id: \\d+</p>\n" +
                                "<p>Name: John</p>\n" +
                                "<p>Email: john12@gmail.com</p>\n" +
                                "<p>Birthday: 2000-07-03</p>\n" +
                                "<p>Roles: REGISTERED_USER</p>"
                ))).andReturn();

        User actUser = userService.getUserByEmail(expEmail);
        assertNotNull(actUser);
    }

    @Test
    public void getById() throws Exception {
        User user = testObjects.createJohn();

        mvc.perform(get(UserController.ROOT_ENDPOINT + "/id/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format(
                        LoginControllerTest.ANONYMOUS_HEADER +
                                "<h1>User</h1>\n" +
                                "<p>Id: %d</p>\n" +
                                "<p>Name: %s</p>\n" +
                                "<p>Email: %s</p>\n" +
                                "<p>Birthday: 1980-03-20</p>\n" +
                                "<p>Roles: REGISTERED_USER</p>",
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                )));
    }


    @Test
    public void getByIdNotFound() throws Exception {
        mvc.perform(get(UserController.ROOT_ENDPOINT + "/id/333"))
                .andExpect(status().isOk())
                .andExpect(content().string(LoginControllerTest.ANONYMOUS_HEADER +
                        "<h1>User</h1>\n" +
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
        MockMultipartFile multipartFile1 = new MockMultipartFile(UserController.PART_NAME,
                "filename1.json", MediaType.APPLICATION_JSON_VALUE, fileContent1.getBytes());

        String fileContent2 = JsonUtil.format("{" +
                "  'name': 'Julia'," +
                "  'email': '%s'," +
                "  'birthday': '2012-09-11'," +
                "  'password': 'pass'" +
                "}", email2
        );
        MockMultipartFile multipartFile2 = new MockMultipartFile(UserController.PART_NAME,
                "filename2.json", MediaType.APPLICATION_JSON_VALUE, fileContent2.getBytes());

        MockMultipartHttpServletRequestBuilder multipartBuilder = MockMvcRequestBuilders
                .fileUpload(UserController.BATCH_UPLOAD_ENDPOINT)
                .file(multipartFile1)
                .file(multipartFile2);

        mvc.perform(multipartBuilder).andExpect(status().isOk());

        assertNotNull(userService.getUserByEmail(email1));
        assertNotNull(userService.getUserByEmail(email2));
    }
}