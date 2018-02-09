package booking.repository.impl;

import booking.domain.User;
import booking.repository.AccountDao;
import booking.repository.UserDao;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class UserDaoImpl extends AbstractDao implements UserDao {

    private final AccountDao accountDao;

    @Autowired
    public UserDaoImpl(@Lazy AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public User create(User user) {
        UserDao.validateUser(user);
        User byEmail = getByEmail(user.getEmail());
        if (Objects.nonNull(byEmail)) {
            throw new IllegalStateException(
                    String.format("Unable to store user: [%s]. User with email: [%s] is already created.", user,
                            user.getEmail()));
        } else {
            Long userId = (Long) getCurrentSession().save(user);
            return user.withId(userId);
        }
    }

    @Override
    public void delete(long userId) {
        accountDao.deleteByUserId(userId);
        Query query = getCurrentSession().createQuery("delete from User where id = :userId ");
        query.setLong("userId", userId);
        query.executeUpdate();
    }

    @Override
    public User getById(long id) {
        return getCurrentSession().get(User.class, id);
    }

    @Override
    public User getByEmail(String email) {
        return ((User) createBlankCriteria(User.class).add(Restrictions.eq("email", email)).uniqueResult());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getAll() {
        return ((List<User>) createBlankCriteria(User.class).list());
    }
}
