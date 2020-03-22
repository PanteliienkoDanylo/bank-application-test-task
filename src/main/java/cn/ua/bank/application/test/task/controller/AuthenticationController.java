package cn.ua.bank.application.test.task.controller;

import cn.ua.bank.application.test.task.configuration.AuthenticatedUser;
import cn.ua.bank.application.test.task.configuration.TokenProvider;
import cn.ua.bank.application.test.task.exception.IncorrectPasswordException;
import cn.ua.bank.application.test.task.model.User;
import cn.ua.bank.application.test.task.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController extends BaseController {

    private final AuthenticationService authenticationService;
    private final TokenProvider tokenProvider;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, TokenProvider tokenProvider) {
        this.authenticationService = authenticationService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody User user) {
        try {
            authenticationService.login(user);
        } catch (IncorrectPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = tokenProvider.newToken();
        tokenProvider.put(token, new AuthenticatedUser(user));
        return ResponseEntity.ok(token);
    }

}
