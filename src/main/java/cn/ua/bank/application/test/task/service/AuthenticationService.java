package cn.ua.bank.application.test.task.service;

import cn.ua.bank.application.test.task.exception.IncorrectPasswordException;
import cn.ua.bank.application.test.task.model.User;
import org.springframework.stereotype.Service;

public interface AuthenticationService {
    User login(User user) throws IncorrectPasswordException;
}
