package cn.ua.bank.application.test.task.repository;

import cn.ua.bank.application.test.task.model.OperationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OperationHistoryRepository extends JpaRepository<OperationHistory, Long> {

    List<OperationHistory> findAllByUserEmail(String email);

    @Modifying
    @Transactional
    @Query("delete from OperationHistory oh where oh.user.email = :email")
    void deleteAllByUserEmail(@Param("email") String email);
}
