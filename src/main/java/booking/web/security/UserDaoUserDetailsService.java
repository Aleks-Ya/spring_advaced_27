package booking.web.security;

import booking.beans.models.User;
import booking.beans.services.UserService;
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
    private final UserService userService;

    @Autowired
    public UserDaoUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = userService.getUsersByName(username);
        if (users.size() > 1) {
            throw new IllegalStateException("More than one user with name '" + username + "'.");
        }
        UserDetails userDetails = null;
        if (!users.isEmpty()) {
            User user = users.get(0);
            String rolesStr = user.getRoles();
            List<SimpleGrantedAuthority> authorities = Collections.emptyList();
            if (rolesStr != null) {
                authorities = Stream.of(rolesStr.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }
            userDetails = new org.springframework.security.core.userdetails.User(
                    user.getName(), user.getPassword(), authorities);
        }
        return userDetails;
    }
}
