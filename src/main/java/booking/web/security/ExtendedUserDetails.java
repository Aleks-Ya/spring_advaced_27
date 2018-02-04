package booking.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class ExtendedUserDetails extends User {

    private final String name;
    private final String email;

    public ExtendedUserDetails(String username,
                        String password,
                        Collection<? extends GrantedAuthority> authorities,
                        String email,
                        String name
    ) {
        super(username, password, authorities);
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
