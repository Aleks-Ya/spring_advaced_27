package booking.web.security;

import booking.BaseWebSecurityTest;
import booking.domain.User;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksey Yablokov
 */
@ContextConfiguration(classes = SimpleController.class)
public class SecurityConfigTest extends BaseWebSecurityTest {

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