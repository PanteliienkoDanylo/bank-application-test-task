package cn.ua.bank.application.test.task.repository;

import cn.ua.bank.application.test.task.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    @Modifying
    @Query("update UserAccount ua set ua.balance = ua.balance + :amount where ua.id = :id")
    void depositMoney(@Param("id") Long id, @Param("amount") BigDecimal amount);

    @Modifying
    @Query("update UserAccount ua set ua.balance = ua.balance - :amount where ua.id = :id")
    void withdrawMoney(@Param("id") Long id, @Param("amount") BigDecimal amount);

}
