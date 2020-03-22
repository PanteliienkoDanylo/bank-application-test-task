package cn.ua.bank.application.test.task.service.impl;

import cn.ua.bank.application.test.task.exception.InvalidDepositAmountException;
import cn.ua.bank.application.test.task.exception.InvalidWithdrawAmountException;
import cn.ua.bank.application.test.task.exception.NotEnoughMoneyException;
import cn.ua.bank.application.test.task.exception.UserNotFoundException;
import cn.ua.bank.application.test.task.model.OperationHistory;
import cn.ua.bank.application.test.task.repository.OperationHistoryRepository;
import cn.ua.bank.application.test.task.repository.UserAccountRepository;
import cn.ua.bank.application.test.task.repository.UserRepository;
import cn.ua.bank.application.test.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserAccountRepository userAccountRepository;
    private final OperationHistoryRepository operationHistoryRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserAccountRepository userAccountRepository, OperationHistoryRepository operationHistoryRepository) {
        this.userRepository = userRepository;
        this.userAccountRepository = userAccountRepository;
        this.operationHistoryRepository = operationHistoryRepository;
    }

    @Override
    @Transactional
    public void depositMoney(String email, BigDecimal amount) throws InvalidDepositAmountException {
        if (BigDecimal.ZERO.compareTo(amount) > 0) throw new InvalidDepositAmountException(amount);
        userAccountRepository.depositMoney(userRepository.findUserAccountIdByEmail(email), amount);
        saveNewOperationHistory(email);
    }

    @Override
    @Transactional
    public void withdrawMoney(String email, BigDecimal amount) throws InvalidWithdrawAmountException, NotEnoughMoneyException {
        if (BigDecimal.ZERO.compareTo(amount) > 0) throw new InvalidWithdrawAmountException(amount);
        if (getBalance(email).compareTo(amount) < 0) throw new NotEnoughMoneyException(email, amount);
        userAccountRepository.withdrawMoney(userRepository.findUserAccountIdByEmail(email), amount);
        saveNewOperationHistory(email);
    }

    @Override
    public List<OperationHistory> getOperationHistories(String email) {
        return operationHistoryRepository.findAllByUserEmail(email);
    }

    @Override
    public BigDecimal getBalance(String email) {
        return userRepository.findBalance(email);
    }

    private void saveNewOperationHistory(String email) throws UserNotFoundException {
        OperationHistory operationHistory = new OperationHistory();
        operationHistory.setUser(userRepository.findById(email).orElseThrow(() -> new UserNotFoundException(email)));
        operationHistoryRepository.save(operationHistory);
    }
}
