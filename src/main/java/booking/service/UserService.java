package booking.service;

import booking.domain.User;

import java.util.List;
import java.util.Objects;

public interface UserService {

    static void validateUser(User user) {
        if (Objects.isNull(user)) {
            throw new NullPointerException("User is [null]");
        }
        if (Objects.isNull(user.getEmail())) {
            throw new NullPointerException("User email is [null]");
        }
    }

    User register(User user);

    void delete(User user);

    User getById(long userId);

    User getByEmail(String email);

    User getCurrentUser();

    List<User> getAll();
}
