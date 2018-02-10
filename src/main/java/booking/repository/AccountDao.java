package booking.repository;

import booking.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDao {

    Account create(Account account);

    List<Account> getAll();

    Optional<Account> getById(long accountId);

    Optional<Account> getByUserId(long userId);

    Account update(Account account);

    void deleteById(long accountId);

    void deleteByUserId(long userId);
}
