package booking.repository.impl;

import booking.domain.Account;
import booking.domain.User;
import booking.repository.AccountDao;
import booking.repository.UserDao;
import booking.service.UserService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountDaoImpl extends AbstractDao implements AccountDao {

    private final UserDao userDao;

    @Autowired
    public AccountDaoImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Account create(Account account) {
        long userId = account.getUser().getId();
        User user = userDao.getById(userId);
        UserService.validateUser(user);
        if (getByUserId(userId) != null) {
            throw new IllegalStateException("User already has account: " + user);
        }
        getCurrentSession().saveOrUpdate(user);
        getCurrentSession().saveOrUpdate(account);
        return account;
    }

    @Override
    public Account getById(long accountId) {
        return getCurrentSession().get(Account.class, accountId);
    }

    @Override
    public Account getByUserId(long userId) {
        Query query = getCurrentSession().createQuery("from Account a where a.user.id = :userId");
        query.setParameter("userId", userId);
        return (Account) query.uniqueResult();
    }

    @Override
    public Account update(Account account) {
        getCurrentSession().saveOrUpdate(account);
        return account;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Account> getAll() {
        return ((List<Account>) createBlankCriteria(Account.class).list());
    }

    @Override
    public void deleteById(long accountId) {
        getCurrentSession().delete(getById(accountId));
    }

    @Override
    public void deleteByUserId(long userId) {
        Query query = getCurrentSession().createQuery("delete from Account where user.id = :userId ");
        query.setLong("userId", userId);
        query.executeUpdate();
    }
}
