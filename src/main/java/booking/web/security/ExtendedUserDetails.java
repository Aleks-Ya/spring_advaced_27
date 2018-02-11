package booking.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ExtendedUserDetails that = (ExtendedUserDetails) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, email);
    }
}
