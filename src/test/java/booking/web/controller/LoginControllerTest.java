package booking.web.controller;

import booking.domain.User;
import booking.repository.config.DataSourceConfig;
import booking.repository.config.DbSessionFactoryConfig;
import booking.repository.config.TestUserServiceConfig;
import booking.service.UserService;
import booking.util.ResourceUtil;
import booking.web.config.FreeMarkerConfig;
import booking.web.config.MvcConfig;
import booking.web.security.Roles;
import booking.web.security.SecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Aleksey Yablokov
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MvcConfig.class, FreeMarkerConfig.class, LoginController.class,
        DataSourceConfig.class, DbSessionFactoryConfig.class, TestUserServiceConfig.class, SecurityConfig.class,
        RootController.class
})
public class LoginControllerTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService userService;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void getLoginForm() throws Exception {
        String expBody = ResourceUtil.resourceToString("login_form.html", LoginController.class);
        mvc.perform(get(LoginController.ENDPOINT)
        ).andExpect(status().isOk())
                .andExpect(content().string(expBody));
    }

    @Test
    public void loginError() throws Exception {
        String expBody = ResourceUtil.resourceToString("login_error.html", LoginController.class);

        MvcResult mvcResult = mvc.perform(post(LoginController.ENDPOINT))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LoginController.ENDPOINT + "?error")).andReturn();

        String redirectedUrl = mvcResult.getResponse().getRedirectedUrl();

        mvc.perform(get(redirectedUrl))
                .andExpect(status().isOk())
                .andExpect(content().string(expBody));
    }

    @Test
    public void logout() throws Exception {
        MockHttpSession session = new MockHttpSession();

        String email = UUID.randomUUID().toString();
        User user = userService.register(new User(email, "John", LocalDate.now(), "abc", Roles.REGISTERED_USER));

        MvcResult mvcResultLogin = mvc.perform(post(LoginController.ENDPOINT).session(session)
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
    }

}
