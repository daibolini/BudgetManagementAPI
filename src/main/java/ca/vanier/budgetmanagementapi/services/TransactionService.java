package ca.vanier.budgetmanagementapi.services;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import ca.vanier.budgetmanagementapi.entity.Transaction;

public interface TransactionService {

    Transaction save(Transaction Transaction);

    Transaction update(Long id, Transaction transactionDetails);

    Optional<Transaction> findById(Long id);

    List<Transaction> findAll();

    void delete(Long id);


    List<Transaction> getAllTransactionsByUserId(Long userId);

    List<Transaction> getExpensesByUserId(Long userId);

    List<Transaction> getIncomesByUserId(Long userId);

    double getTotalIncomeByUser(Long userId);

    double getTotalExpensesByUser(Long userId);
    
    double getUserBalance(Long userId);

    double getUserBalanceByCategory(Long userId, Long categoryId);

    Map<String, Double> getUserTransactionCategorySummary(Long userId);



}