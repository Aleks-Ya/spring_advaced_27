package booking.service.impl;

import booking.BaseServiceTest;
import booking.domain.Account;
import booking.domain.User;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class AccountServiceImplTest extends BaseServiceTest {

    @Test
    public void create() {
        User user = testObjects.createJohn();
        Account expAccount = accountService.create(new Account(user, BigDecimal.valueOf(1000)));
        Account actAccount = accountService.getById(expAccount.getId());
        assertThat(actAccount, equalTo(expAccount));
    }

    @Test(expected = IllegalStateException.class)
    public void createUseHasSingleAccount() {
        User user = testObjects.createJohn();
        accountService.create(new Account(user, BigDecimal.valueOf(1000)));
        accountService.create(new Account(user, BigDecimal.valueOf(1000)));
    }

    @Test
    public void delete() {
        User user = testObjects.createJohn();
        long accountId = accountService.create(new Account(user, BigDecimal.valueOf(1000))).getId();
        accountService.delete(accountId);
        assertNull(accountService.getById(accountId));
        assertThat(accountService.getAll(), emptyIterable());
    }

    @Test
    public void getAll() {
        assertThat(accountService.getAll(), emptyIterable());

        Account account1 = testObjects.createAccount();
        Account account2 = testObjects.createAccount();
        List<Account> accounts = accountService.getAll();

        assertThat(accounts, containsInAnyOrder(account1, account2));
    }

    @Test
    public void getById() {
        int notExistsAccountId = 12345;
        assertNull(accountService.getById(notExistsAccountId));

        Account account = testObjects.createAccount();
        assertThat(accountService.getById(account.getId()), equalTo(account));
    }

    @Test
    public void getByUserId() {
        Account account = testObjects.createAccount();
        User user = account.getUser();
        assertThat(accountService.getByUserId(user.getId()), equalTo(account));
    }

    @Test
    public void withdrawal() {
        Account oldAccount = testObjects.createAccount();
        BigDecimal withdrawalAmount = BigDecimal.valueOf(300);
        Account actAccount = accountService.withdrawal(oldAccount, withdrawalAmount);
        assertThat(actAccount.getAmount(), closeTo(BigDecimal.valueOf(9700), BigDecimal.ZERO));
    }

    @Test
    public void refill() {
        Account oldAccount = testObjects.createAccount();
        BigDecimal refillAmount = BigDecimal.valueOf(300);
        Account actAccount = accountService.refill(oldAccount, refillAmount);
        assertThat(actAccount.getAmount(), closeTo(BigDecimal.valueOf(10300), BigDecimal.ZERO));
    }
}
