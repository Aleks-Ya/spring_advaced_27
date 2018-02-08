package booking.repository;

import booking.domain.User;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

public interface UserDao {

    static void validateUser(User user) {
        if (Objects.isNull(user)) {
            throw new NullPointerException("User is [null]");
        }
        if (Objects.isNull(user.getEmail())) {
            throw new NullPointerException("User's email is [null]: [" + userToPublicString(user) + "]");
        }
        if (Objects.isNull(user.getName())) {
            throw new NullPointerException("User's name is [null]: [" + userToPublicString(user) + "]");
        }
        if (Objects.isNull(user.getPassword())) {
            throw new NullPointerException("User's password is [null]: [" + userToPublicString(user) + "]");
        }
    }

    static String userToPublicString(User user) {
        return format("User {email='%s', name='%s', birthday='%s'}", user.getEmail(), user.getName(), user.getBirthday());
    }

    User create(User user);

    void delete(long userId);

    User getById(long id);

    User getByEmail(String email);

    List<User> getAll();
}
