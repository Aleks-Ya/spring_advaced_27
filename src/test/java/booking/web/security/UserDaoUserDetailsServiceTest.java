package booking.web.security;

import booking.beans.models.User;
import booking.beans.services.UserService;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.Arrays;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Aleksey Yablokov
 */
public class UserDaoUserDetailsServiceTest {
    private static final String username = "the_user";

    private static User makeUserWithAuthorities(String roles) {
        return new User("john@mail.ru", "John", LocalDate.of(2003, 1, 15), "pass", roles);
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
        service.loadUserByUsername(username);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void twoUsers() {
        UserService userService = mock(UserService.class);
        when(userService.getUsersByName(username)).thenReturn(Arrays.asList(mock(User.class), mock(User.class)));
        UserDaoUserDetailsService service = new UserDaoUserDetailsService(userService);
        service.loadUserByUsername(username);
    }

    @Test
    public void nullAuthorities() {
        UserService userService = mock(UserService.class);
        User user = makeUserWithAuthorities(null);
        when(userService.getUsersByName(username)).thenReturn(singletonList(user));
        UserDaoUserDetailsService service = new UserDaoUserDetailsService(userService);
        UserDetails userDetails = service.loadUserByUsername(username);
        assertThat(userDetails.getAuthorities(), emptyIterable());
    }

    @Test
    public void twoAuthorities() {
        UserService userService = mock(UserService.class);
        User user = makeUserWithAuthorities("user" + UserDaoUserDetailsService.ROLES_DELIMITER + "admin");
        when(userService.getUsersByName(username)).thenReturn(singletonList(user));

        UserDaoUserDetailsService service = new UserDaoUserDetailsService(userService);
        UserDetails userDetails = service.loadUserByUsername(username);

        assertThat(userDetails.getAuthorities(),
                containsInAnyOrder(
                        new SimpleGrantedAuthority("user"),
                        new SimpleGrantedAuthority("admin")
                ));
    }
}