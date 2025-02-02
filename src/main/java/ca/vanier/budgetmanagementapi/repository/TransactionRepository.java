package ca.vanier.budgetmanagementapi.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.vanier.budgetmanagementapi.entity.Transaction;


@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findAllByUserId(Long userId);

    List<Transaction> findAllByUserIdAndCategoryIdNot(Long userId, Long categoryId);

    List<Transaction> findAllByUserIdAndCategoryId(Long userId, Long categoryId);

    List<Transaction> findAllByUserIdAndCreatedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
