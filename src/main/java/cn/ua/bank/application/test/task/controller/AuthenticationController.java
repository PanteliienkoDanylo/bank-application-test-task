package cn.ua.bank.application.test.task.controller;

import cn.ua.bank.application.test.task.configuration.security.AuthenticatedUser;
import cn.ua.bank.application.test.task.configuration.security.TokenProvider;
import cn.ua.bank.application.test.task.exception.IncorrectPasswordException;
import cn.ua.bank.application.test.task.model.User;
import cn.ua.bank.application.test.task.service.AuthenticationService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Authentication", description = "Authentication operation")
public class AuthenticationController extends BaseController {

    private final AuthenticationService authenticationService;
    private final TokenProvider tokenProvider;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, TokenProvider tokenProvider) {
        this.authenticationService = authenticationService;
        this.tokenProvider = tokenProvider;
    }

    @ApiOperation(value = "Login")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully login"),
            @ApiResponse(code = 401, message = "You are not authorized - incorrect password"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    })
    @PostMapping("login")
    public ResponseEntity login(
            @ApiParam(value = "User credentials: email and password", required = true) @RequestBody UserCredentials userCredentials) {
        try {
            authenticationService.login(new User(userCredentials.email, userCredentials.password));
        } catch (IncorrectPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(INVALID_PASSWORD);
        }
        String token = tokenProvider.newToken();
        tokenProvider.put(token, new AuthenticatedUser(new User(userCredentials.email, userCredentials.password)));
        return ResponseEntity.ok(token);
    }

    private static class UserCredentials {
        public String email;
        public String password;
    }

}
