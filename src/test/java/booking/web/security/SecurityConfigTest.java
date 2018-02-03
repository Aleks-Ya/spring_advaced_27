package booking.web.security;

import booking.BaseWebTest;
import booking.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@ContextConfiguration(classes = {SimpleController.class, SecurityConfig.class})
public class SecurityConfigTest extends BaseWebTest {
    @Before
    @Override
    public void initMockMvc() {
        mvc = MockMvcBuilders
                .webAppContextSetup(wc)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void getByIdNotFound() throws Exception {
        String password = "the_password";
        String email = "the@mail.ru";
        User user = new User(email, "the_username", LocalDate.of(2000, 1, 14), password, null);

        userService.register(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(email, password);
        mvc.perform(get("/abc").with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(content().string("abc"));
    }
}