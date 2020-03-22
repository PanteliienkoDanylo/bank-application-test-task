package cn.ua.bank.application.test.task.exception;

public abstract class BankApplicationTestTaskException extends RuntimeException {

    public BankApplicationTestTaskException(String message) {
        super(message);
    }

    public BankApplicationTestTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
