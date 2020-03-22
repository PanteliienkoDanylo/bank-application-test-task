package cn.ua.bank.application.test.task.exception;

import cn.ua.bank.application.test.task.model.User;

public class IncorrectPasswordException extends BankApplicationTestTaskException {

    public IncorrectPasswordException(User user) {
        super("Incorrect password for user: "  + user.getEmail());
    }
}
