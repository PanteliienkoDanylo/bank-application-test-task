package cn.ua.bank.application.test.task.exception;

import java.math.BigDecimal;

public class InvalidDepositAmountException extends BankApplicationTestTaskException {

    public InvalidDepositAmountException(BigDecimal amount) {
        super("Invalid withdraw amount: " + amount);
    }


}
