package booking.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Information about money that users possess.
 *
 * @author Aleksey Yablokov
 */
@SuppressWarnings("unused")
public class Account {
    private long id;
    private User user;
    private BigDecimal amount;

    public Account() {
    }

    public Account(long id, User user, BigDecimal amount) {
        this.id = id;
        this.user = user;
        this.amount = amount;
    }

    public Account(User user, BigDecimal amount) {
        this.user = user;
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account withAmount(BigDecimal amount) {
        return new Account(getId(), getUser(), amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id &&
                Objects.equals(user, account.user) &&
                (amount.compareTo(account.amount) == 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, amount);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", user=" + user +
                ", amount=" + amount +
                '}';
    }
}
