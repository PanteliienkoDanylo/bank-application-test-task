package cn.ua.bank.application.test.task.controller;

import cn.ua.bank.application.test.task.exception.InvalidDepositAmountException;
import cn.ua.bank.application.test.task.exception.InvalidWithdrawAmountException;
import cn.ua.bank.application.test.task.exception.NotEnoughMoneyException;
import cn.ua.bank.application.test.task.model.OperationHistory;
import cn.ua.bank.application.test.task.service.UserService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Api(value = "User", description = "User operations")
@RestController
@RequestMapping("/bank-app-test-task/api/user")
public class UserController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Deposit money", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Successfully deposit money"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 406, message = "Amount of money is not valid - less then 0")
    })
    @PostMapping("balance/deposit")
    public ResponseEntity<String> depositMoney(
            @ApiParam(value = "Amount of desired deposit money") @RequestBody Double amount) {
        try {
            userService.depositMoney(getAuthenticatedUser().getUsername(), BigDecimal.valueOf(amount));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(SUCCESS);
        } catch (InvalidDepositAmountException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @ApiOperation(value = "Withdraw money")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Successfully withdraw money"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 406, message = "Amount of money is not valid - less then 0 or not enough money on balance")
    })
    @PostMapping("balance/withdraw")
    public ResponseEntity<String> withdrawMoney(
            @ApiParam(value = "Amount of desired withdraw money") @RequestBody Double amount) {
        try {
            userService.withdrawMoney(getAuthenticatedUser().getUsername(), BigDecimal.valueOf(amount));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(SUCCESS);
        } catch (InvalidWithdrawAmountException | NotEnoughMoneyException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @ApiOperation(value = "Get balance", response = BigDecimal.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully return user balance"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    })
    @GetMapping("balance")
    public ResponseEntity<BigDecimal> getBalance() {
        return ResponseEntity.ok(userService.getBalance(getAuthenticatedUser().getUsername()));
    }

    @ApiOperation(value = "Get operation history", response = OperationHistory.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully return operation histories"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    })
    @GetMapping("operation-histories")
    public ResponseEntity<List<OperationHistory>> getOperationHistories() {
        return ResponseEntity.ok(userService.getOperationHistories(getAuthenticatedUser().getUsername()));
    }

}
