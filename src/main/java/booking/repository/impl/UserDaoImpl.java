package booking.repository.impl;

import booking.domain.User;
import booking.exception.BookingExceptionFactory;
import booking.repository.AccountDao;
import booking.repository.UserDao;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.Optional;

public class UserDaoImpl extends AbstractDao implements UserDao {

    private final AccountDao accountDao;

    @Autowired
    public UserDaoImpl(@Lazy AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public User create(User user) {
        UserDao.validateUser(user);
        String email = user.getEmail();
        getByEmail(email).ifPresent(existsUser -> BookingExceptionFactory.userAlreadyExistWithEmail(email));
        Long userId = (Long) getCurrentSession().save(user);
        return user.withId(userId);
    }

    @Override
    public void delete(long userId) {
        accountDao.deleteByUserId(userId);
        Query query = getCurrentSession().createQuery("delete from User where id = :userId ");
        query.setLong("userId", userId);
        query.executeUpdate();
    }

    @Override
    public Optional<User> getById(long id) {
        return Optional.ofNullable(getCurrentSession().get(User.class, id));
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return Optional.ofNullable(
                (User) createBlankCriteria(User.class).add(Restrictions.eq("email", email)).uniqueResult());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getAll() {
        return ((List<User>) createBlankCriteria(User.class).list());
    }
}
