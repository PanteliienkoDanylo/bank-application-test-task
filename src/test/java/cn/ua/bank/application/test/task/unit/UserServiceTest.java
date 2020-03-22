package cn.ua.bank.application.test.task.unit;

import cn.ua.bank.application.test.task.exception.InvalidDepositAmountException;
import cn.ua.bank.application.test.task.exception.InvalidWithdrawAmountException;
import cn.ua.bank.application.test.task.exception.NotEnoughMoneyException;
import cn.ua.bank.application.test.task.model.User;
import cn.ua.bank.application.test.task.repository.OperationHistoryRepository;
import cn.ua.bank.application.test.task.repository.UserRepository;
import cn.ua.bank.application.test.task.service.AuthenticationService;
import cn.ua.bank.application.test.task.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceTest {

    private static User testUser = new User("testAccountUser@gmail.com", "qwerty");

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OperationHistoryRepository operationHistoryRepository;

    @BeforeEach
    void addTestUserWithClearAccount() {
        authenticationService.login(testUser);
    }

    @AfterEach
    void deleteTestUser() {
        userRepository.deleteById(testUser.getEmail());
        operationHistoryRepository.deleteAllByUserEmail(testUser.getEmail());
    }

    @Test
    void depositAndWithDrawMoneyAndOperationHistories() {
        userService.depositMoney(testUser.getEmail(), BigDecimal.ONE);
        userService.depositMoney(testUser.getEmail(), BigDecimal.TEN);
        userService.depositMoney(testUser.getEmail(), BigDecimal.TEN);
        userService.withdrawMoney(testUser.getEmail(), BigDecimal.ONE);
        userService.withdrawMoney(testUser.getEmail(), BigDecimal.ONE);
        assertEquals(userService.getBalance(testUser.getEmail()).compareTo(BigDecimal.valueOf(19)), 0);
        assertEquals(userService.getOperationHistories(testUser.getEmail()).size(), 5);
    }

    @Test
    void depositInvalidAmountOfMoney_expectInvalidDepositAmountException() {
        Assertions.assertThrows(InvalidDepositAmountException.class, () -> userService.depositMoney(testUser.getEmail(), BigDecimal.valueOf(-10)));
    }

    @Test
    void withdrawInvalidAmountOfMoney_expectInvalidWithdrawAmountException() {
        Assertions.assertThrows(InvalidWithdrawAmountException.class, () -> userService.withdrawMoney(testUser.getEmail(), BigDecimal.valueOf(-10)));
    }

    @Test
    void withdrawAmountWhichMoreThenBalance_expectNotEnoughMoneyException() {
        Assertions.assertThrows(NotEnoughMoneyException.class, () -> userService.withdrawMoney(testUser.getEmail(), BigDecimal.TEN));
    }


}
