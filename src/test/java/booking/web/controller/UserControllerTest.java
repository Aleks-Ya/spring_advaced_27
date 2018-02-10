package booking.web.controller;

import booking.BaseWebTest;
import booking.domain.User;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;

import static booking.web.controller.RootControllerTest.NAVIGATOR;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        User actUser = userService.getByEmail(expEmail);
        assertNotNull(actUser);
    }

    @Test
    public void getById() throws Exception {
        User user = to.createJohn();

        mvc.perform(get(UserController.ROOT_ENDPOINT + "/" + user.getId()))
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
        mvc.perform(get(UserController.ROOT_ENDPOINT + "/333"))
                .andExpect(status().isOk())
                .andExpect(content().string(LoginControllerTest.ANONYMOUS_HEADER + NAVIGATOR +
                        "<h1>User is not found</h1>\n" +
                        "<p>User is not found by id 333</p>"));
    }

}