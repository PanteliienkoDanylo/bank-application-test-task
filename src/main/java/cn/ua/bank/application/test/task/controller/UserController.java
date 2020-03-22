package cn.ua.bank.application.test.task.controller;

import cn.ua.bank.application.test.task.exception.InvalidDepositAmountException;
import cn.ua.bank.application.test.task.exception.InvalidWithdrawAmountException;
import cn.ua.bank.application.test.task.exception.NotEnoughMoneyException;
import cn.ua.bank.application.test.task.model.OperationHistory;
import cn.ua.bank.application.test.task.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/bank-app-test-task/api/user")
public class UserController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("balance/deposit")
    public ResponseEntity<String> depositMoney(@RequestBody Double amount) {
        try {
            userService.depositMoney(getAuthenticatedUser().getUsername(), BigDecimal.valueOf(amount));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(SUCCESS);
        } catch (InvalidDepositAmountException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @PostMapping("balance/withdraw")
    public ResponseEntity<String> withdrawMoney(@RequestBody Double amount) {
        try {
            userService.withdrawMoney(getAuthenticatedUser().getUsername(), BigDecimal.valueOf(amount));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(SUCCESS);
        } catch (InvalidWithdrawAmountException | NotEnoughMoneyException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @GetMapping("balance")
    public ResponseEntity<BigDecimal> getBalance() {
        return ResponseEntity.ok(userService.getBalance(getAuthenticatedUser().getUsername()));
    }

    @GetMapping("operation-histories")
    public ResponseEntity<List<OperationHistory>> getOperationHistories() {
        return ResponseEntity.ok(userService.getOperationHistories(getAuthenticatedUser().getUsername()));
    }

}
