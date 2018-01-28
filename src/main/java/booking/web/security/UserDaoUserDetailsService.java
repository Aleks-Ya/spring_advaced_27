package booking.web.security;

import booking.domain.User;
import booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found by email: " + email);
        }
        String rolesStr = user.getRoles();
        List<SimpleGrantedAuthority> authorities = Collections.emptyList();
        if (rolesStr != null) {
            authorities = Stream.of(rolesStr.split(ROLES_DELIMITER))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return new ExtendedUserDetails(email, user.getPassword(), authorities, email, user.getName());
    }
}
