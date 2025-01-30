package ca.vanier.budgetmanagementapi.services;


import java.util.List;
import java.util.Optional;

import ca.vanier.budgetmanagementapi.entity.Transaction;

public interface TransactionService {

    Transaction save(Transaction Transaction);

    Transaction update(Long id, Transaction transactionDetails);

    Optional<Transaction> findById(Long id);

    List<Transaction> findAll();

    void delete(Long id);

}