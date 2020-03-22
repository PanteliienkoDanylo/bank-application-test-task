package cn.ua.bank.application.test.task.repository;


import cn.ua.bank.application.test.task.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    @Query("select u.userAccount.balance from User u where u.email = :email")
    BigDecimal findBalance(@Param("email") String email);

    @Query("select u.userAccount.id from User u where u.email = :email")
    Long findUserAccountIdByEmail(@Param("email") String email);

}
