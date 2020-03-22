package cn.ua.bank.application.test.task.exception;

import java.math.BigDecimal;

public class InvalidWithdrawAmountException extends BankApplicationTestTaskException {

    public InvalidWithdrawAmountException(BigDecimal amount) {
        super("Invalid deposit amount: " + amount);
    }


}
