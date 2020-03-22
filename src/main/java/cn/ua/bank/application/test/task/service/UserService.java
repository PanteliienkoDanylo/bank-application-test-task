package cn.ua.bank.application.test.task.service;

import cn.ua.bank.application.test.task.exception.InvalidDepositAmountException;
import cn.ua.bank.application.test.task.exception.InvalidWithdrawAmountException;
import cn.ua.bank.application.test.task.exception.NotEnoughMoneyException;
import cn.ua.bank.application.test.task.model.OperationHistory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {

    void depositMoney(String email, BigDecimal amount) throws InvalidDepositAmountException;

    void withdrawMoney(String email, BigDecimal amount) throws InvalidWithdrawAmountException, NotEnoughMoneyException;

    List<OperationHistory> getOperationHistories(String email);

    BigDecimal getBalance(String email);
}
