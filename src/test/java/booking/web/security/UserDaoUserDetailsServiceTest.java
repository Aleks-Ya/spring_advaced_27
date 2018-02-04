package booking.web.security;

import booking.domain.User;
import booking.service.UserService;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDaoUserDetailsServiceTest {
    private static final String email = "john@mail.ru";

    private static User makeUserWithAuthorities(String roles) {
        return new User(email, "John", LocalDate.of(2003, 1, 15), "pass", roles);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void nUser() {
        UserService userService = mock(UserService.class);
        UserDaoUserDetailsService service = new UserDaoUserDetailsService(userService);
        service.loadUserByUsername(null);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void noUser() {
        UserService userService = mock(UserService.class);
        UserDaoUserDetailsService service = new UserDaoUserDetailsService(userService);
        service.loadUserByUsername(email);
    }

    @Test
    public void nullAuthorities() {
        UserService userService = mock(UserService.class);
        User user = makeUserWithAuthorities(null);
        when(userService.getUserByEmail(email)).thenReturn(user);
        UserDaoUserDetailsService service = new UserDaoUserDetailsService(userService);
        UserDetails userDetails = service.loadUserByUsername(email);
        assertThat(userDetails.getAuthorities(), emptyIterable());
    }

    @Test
    public void twoAuthorities() {
        UserService userService = mock(UserService.class);
        String role1 = "USER";
        String role2 = "ADMIN";
        User user = makeUserWithAuthorities(role1 + UserDaoUserDetailsService.ROLES_DELIMITER + role2);
        when(userService.getUserByEmail(email)).thenReturn(user);

        UserDaoUserDetailsService service = new UserDaoUserDetailsService(userService);
        UserDetails userDetails = service.loadUserByUsername(email);

        assertThat(userDetails.getAuthorities(),
                containsInAnyOrder(
                        new SimpleGrantedAuthority(UserDaoUserDetailsService.SPRING_ROLE_PREFIX + role1),
                        new SimpleGrantedAuthority(UserDaoUserDetailsService.SPRING_ROLE_PREFIX + role2)
                ));
    }
}