package booking.web.security;

import booking.beans.configuration.TestUserServiceConfiguration;
import booking.beans.configuration.db.DataSourceConfiguration;
import booking.beans.configuration.db.DbSessionFactory;
import booking.beans.models.User;
import booking.beans.services.UserService;
import booking.web.EnableWebMvcConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {EnableWebMvcConfig.class, SimpleController.class, SecurityConfig.class,
        TestUserServiceConfiguration.class,  DataSourceConfiguration.class, DbSessionFactory.class})
public class SecurityConfigTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserService userService;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void getByIdNotFound() throws Exception {
        String username = "the_username";
        String password = "the_password";
        User user = new User("the@mail.ru", username, LocalDate.of(2000, 1, 14), password, null);

        userService.register(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
        mvc.perform(get("/abc").with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(content().string("abc"));
    }
}