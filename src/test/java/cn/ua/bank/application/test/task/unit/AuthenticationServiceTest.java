package cn.ua.bank.application.test.task.unit;

import cn.ua.bank.application.test.task.exception.IncorrectPasswordException;
import cn.ua.bank.application.test.task.model.AccountStatement;
import cn.ua.bank.application.test.task.model.User;
import cn.ua.bank.application.test.task.repository.UserRepository;
import cn.ua.bank.application.test.task.service.AuthenticationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AuthenticationServiceTest {

    private static User testExistUser;
    private static User testNewUser;
    private static User testExistedUser;
    private static User testIncorrectPasswordUser;

    static {
        testExistUser = new User("testExist@gmail.com", "qwerty");
        testNewUser = new User("testNew@gmail.com", "qwerty");
        testExistedUser = new User("testExist@gmail.com", "qwerty");
        testIncorrectPasswordUser = new User("testExist@gmail.com", "qwerty123");
    }

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void addTestUser() {
        userRepository.save(testExistUser);
    }

    @AfterEach
    void deleteTestUser() {
        userRepository.delete(testExistUser);
    }

    @Test
    void loginWithNewUser_addNewUserToDBAndReturnIt() {
        assertEquals(userRepository.count(), 1);
        User addedUser = authenticationService.login(testNewUser);
        assertEquals(addedUser.getEmail(), testNewUser.getEmail());
        assertEquals(addedUser.getPassword(), testNewUser.getPassword());
        assertEquals(addedUser.getUserAccount().getBalance().compareTo(BigDecimal.ZERO), 0);
        assertEquals(addedUser.getUserAccount().getStatement(), AccountStatement.OPENED);
        assertEquals(userRepository.count(), 2);
        userRepository.deleteById(addedUser.getEmail());
    }

    @Test
    void loginWithExistUser_returnExistUser() {
        assertEquals(userRepository.count(), 1);
        User existedUser = authenticationService.login(testExistedUser);
        assertEquals(existedUser.getEmail(), testExistedUser.getEmail());
        assertEquals(existedUser.getPassword(), testExistedUser.getPassword());
        assertEquals(existedUser.getUserAccount().getBalance().compareTo(BigDecimal.ZERO), 0);
        assertEquals(existedUser.getUserAccount().getStatement(), AccountStatement.OPENED);
        assertEquals(userRepository.count(), 1);
    }

    @Test
    void loginWithIncorrectPassword_throwIncorrectPasswordException() {
        Assertions.assertThrows(IncorrectPasswordException.class, () -> authenticationService.login(testIncorrectPasswordUser));
    }
}
