package booking.beans.daos;

import booking.beans.models.User;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/2/2016
 * Time: 11:38 AM
 */
public interface UserDAO {

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

    void delete(User user);

    User get(long id);

    User getByEmail(String email);

    List<User> getAllByName(String name);

    List<User> getAll();
}
