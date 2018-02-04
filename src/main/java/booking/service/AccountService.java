package booking.service;

import booking.domain.Account;

import java.math.BigDecimal;
import java.util.List;

/**
 * Operates users' money.
 *
 * @author Aleksey Yablokov
 */
public interface AccountService {
    Account create(Account account);

    List<Account> getAll();

    Account getById(long accountId);

    Account getByUserId(long userId);

    Account withdrawal(Account account, BigDecimal withdrawalAmount);

    Account refill(Account account, BigDecimal refillAmount);

    void delete(long accountId);
}
