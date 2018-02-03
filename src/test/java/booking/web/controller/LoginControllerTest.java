package booking.web.controller;

import booking.BaseWebSecurityTest;
import booking.domain.User;
import booking.util.ResourceUtil;
import booking.web.security.Roles;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Aleksey Yablokov
 */
@ContextConfiguration(classes = {LoginController.class, RootController.class})
public class LoginControllerTest extends BaseWebSecurityTest {
    public static final String ANONYMOUS_HEADER = "User: Anonymous (<a href='/login'>login</a>)\n";

    @Test
    public void getLoginForm() throws Exception {
        String expBody = ResourceUtil.resourceToString("login_form.html", LoginController.class);
        mvc.perform(get(LoginController.LOGIN_ENDPOINT)
        ).andExpect(status().isOk())
                .andExpect(content().string(expBody));
    }

    @Test
    public void loginError() throws Exception {
        String expBody = ResourceUtil.resourceToString("login_error.html", LoginController.class);

        MvcResult mvcResult = mvc.perform(post(LoginController.LOGIN_ENDPOINT))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LoginController.LOGIN_ENDPOINT + "?error")).andReturn();

        String redirectedUrl = mvcResult.getResponse().getRedirectedUrl();

        mvc.perform(get(redirectedUrl))
                .andExpect(status().isOk())
                .andExpect(content().string(expBody));
    }

    @Test
    public void logout() throws Exception {
        MockHttpSession session = new MockHttpSession();

        String email = "dan@gmail.com";
        User user = userService.register(new User(email, "John", LocalDate.now(), "abc", Roles.REGISTERED_USER));

        MvcResult mvcResultLogin = mvc.perform(post(LoginController.LOGIN_ENDPOINT).session(session)
                .param("username", user.getEmail())
                .param("password", user.getPassword())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        ).andExpect(status().is3xxRedirection()).andReturn();

        String redirectedUrlLogin = mvcResultLogin.getResponse().getRedirectedUrl();

        String rootBody = ResourceUtil.resourceToString("logout_root.html", LoginController.class);
        mvc.perform(get(redirectedUrlLogin).session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(rootBody));


        MvcResult mvcResultLogout = mvc.perform(get("/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout")).andReturn();

        String redirectedUrlLogout = mvcResultLogout.getResponse().getRedirectedUrl();

        String logoutBody = ResourceUtil.resourceToString("logout.html", LoginController.class);
        mvc.perform(get(redirectedUrlLogout).session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(logoutBody));

        userService.delete(user);
    }

}
