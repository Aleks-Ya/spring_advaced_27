package beans.daos.inmemory;

import beans.daos.UserDAO;
import beans.models.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/2/2016
 * Time: 11:41 AM
 */
@Repository("inMemoryUserDAO")
public class InMemoryUserDAO implements UserDAO {

    private static final Map<Long, User>        db           = new TreeMap<>();
    private static final Map<String, User>      dbEmailIndex = new TreeMap<>();
    private static final Map<String, Set<User>> dbNameIndex  = new TreeMap<>();

    public User create(User user) {
        UserDAO.validateUser(user);

        if (db.containsKey(user.getId()))
            throw new IllegalStateException(
                    String.format("Unable to store user: [%s]. User with id: [%s] is already created.", user, user.getId()));
        if (dbEmailIndex.containsKey(user.getEmail()))
            throw new IllegalStateException(
                    String.format("Unable to store user: [%s]. User with email: [%s] is already created.", user,
                                  user.getEmail()));

        final User userToStore = user.getId() < 0 ? user.withId(db.size()) : user;

        db.put(userToStore.getId(), userToStore);
        dbEmailIndex.put(userToStore.getEmail(), userToStore);

        dbNameIndex.putIfAbsent(userToStore.getName(), new HashSet<>());
        dbNameIndex.get(userToStore.getName()).add(userToStore);

        return userToStore;
    }

    public void delete(User user) {
        UserDAO.validateUser(user);

        db.remove(user.getId());
        dbEmailIndex.remove(user.getEmail());

        final Set<User> users = dbNameIndex.get(user.getName());
        if (Objects.nonNull(users))
            users.remove(user);
    }


    public User get(long id) {
        return db.get(id);
    }

    public User getByEmail(String email) {
        return dbEmailIndex.get(email);
    }

    public List<User> getAllByName(String name) {
        return dbNameIndex.get(name).stream().collect(Collectors.toList());
    }

    @Override
    public List<User> getAll() {
        return db.values().stream().collect(Collectors.toList());
    }
}
