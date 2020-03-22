package cn.ua.bank.application.test.task.exception;

public class UserNotFoundException extends BankApplicationTestTaskException {

    public UserNotFoundException(String email) {
        super("User with email: " + email +  " was not found");
    }
}
