package booking.web.security;

import booking.domain.User;
import booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UserDaoUserDetailsService implements UserDetailsService {
    public static final String ROLES_DELIMITER = ",";
    /**
     * Spring Security adds this prefix to all role names.
     *
     * @see ExpressionUrlAuthorizationConfigurer.AuthorizedUrl#hasRole(java.lang.String)
     */
    public static final String SPRING_ROLE_PREFIX = "ROLE_";
    private final UserService userService;

    @Autowired
    public UserDaoUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        if (email == null) {
            throw new UsernameNotFoundException("email is null");
        }
        User user = userService.getByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found by email: " + email);
        }
        String rolesStr = user.getRoles();
        List<SimpleGrantedAuthority> authorities = rolesStrToAuthorities(rolesStr);
        return new ExtendedUserDetails(email, user.getPassword(), authorities, email, user.getName());
    }

    public static List<SimpleGrantedAuthority> rolesStrToAuthorities(String rolesStr) {
        List<SimpleGrantedAuthority> authorities = Collections.emptyList();
        if (rolesStr != null) {
            authorities = Stream.of(rolesStr.split(ROLES_DELIMITER))
                    .map(role -> SPRING_ROLE_PREFIX + role)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return authorities;
    }
}
