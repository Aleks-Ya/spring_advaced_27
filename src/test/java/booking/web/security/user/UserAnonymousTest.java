package booking.web.security.user;

import booking.BaseWebSecurityTest;
import booking.domain.User;
import booking.web.controller.UserController;
import booking.web.security.Roles;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Check behaviour for an unauthorized user.
 *
 * @author Aleksey Yablokov
 */
@ContextConfiguration(classes = UserController.class)
public class UserAnonymousTest extends BaseWebSecurityTest {

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

        String encodedPassword = actUser.getPassword();
        assertThat(actUser.getEmail(), equalTo(expEmail));
        assertThat(actUser.getName(), equalTo(expName));
        assertThat(actUser.getBirthday(), equalTo(expBirthday));
        assertThat("Password is stored in DB in unencoded form", encodedPassword, not(equalTo(rawPassword)));
        assertThat(actUser.getRoles(), equalTo(Roles.REGISTERED_USER));
    }

    @Test
    public void getById() {
        assertRedirectToLoginPage(get(UserController.ROOT_ENDPOINT + "/id/1"));
    }


    @Test
    public void batchUpload() {
        assertRedirectToLoginPage(fileUpload(UserController.BATCH_UPLOAD_ENDPOINT));
    }

}