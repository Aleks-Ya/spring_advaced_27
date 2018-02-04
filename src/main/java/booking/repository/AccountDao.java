package booking.repository;

import booking.domain.Account;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface AccountDao {

    Account create(Account account);

    List<Account> getAll();

    Account getById(long accountId);

    Account getByUserId(long userId);

    Account update(Account account);

    void delete(long accountId);
}
