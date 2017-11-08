package beans.daos;

import beans.models.User;

import java.util.List;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/2/2016
 * Time: 11:38 AM
 */
public interface UserDAO {

    User create(User user);

    void delete(User user);

    User get(long id);

    User getByEmail(String email);

    List<User> getAllByName(String name);

    List<User> getAll();

    static void validateUser(User user) {
        if (Objects.isNull(user)) {
            throw new NullPointerException("User is [null]");
        }
        if (Objects.isNull(user.getEmail())) {
            throw new NullPointerException("User's email is [null]. User: [" + user + "]");
        }
        if (Objects.isNull(user.getName())) {
            throw new NullPointerException("User's name is [null]. User: [" + user + "]");
        }
    }
}
