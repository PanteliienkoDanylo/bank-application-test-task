package cn.ua.bank.application.test.task.exception;

import java.math.BigDecimal;

public class NotEnoughMoneyException extends BankApplicationTestTaskException {

    public NotEnoughMoneyException(String email, BigDecimal amount) {
        super("User with email: " + email + " has not enough money for withdrawing amount: " + amount);
    }
}
