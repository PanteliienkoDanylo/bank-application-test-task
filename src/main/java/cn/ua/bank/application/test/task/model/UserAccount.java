package cn.ua.bank.application.test.task.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "user_account")
public class UserAccount {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "balance", precision = 11, scale = 2)
    private BigDecimal balance;

    @Column(name = "statement")
    @Enumerated(EnumType.STRING)
    private AccountStatement statement;

    public UserAccount() {
        this.balance = BigDecimal.ZERO;
        this.statement = AccountStatement.OPENED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountStatement getStatement() {
        return statement;
    }

    public void setStatement(AccountStatement statement) {
        this.statement = statement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount)) return false;
        UserAccount that = (UserAccount) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(balance, that.balance) &&
                statement == that.statement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, statement);
    }
}
