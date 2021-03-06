package cn.ua.bank.application.test.task.controller;

import cn.ua.bank.application.test.task.configuration.security.AuthenticatedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank-app-test-task/api")
public abstract class BaseController {

    protected static final String SUCCESS = "success";
    protected static final String INVALID_PASSWORD = "invalid password";

    protected AuthenticatedUser getAuthenticatedUser() {
        return (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
