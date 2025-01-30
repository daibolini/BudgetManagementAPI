package ca.vanier.budgetmanagementapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.vanier.budgetmanagementapi.Exceptions.UserNotFoundException;
import ca.vanier.budgetmanagementapi.entity.Transaction;
import ca.vanier.budgetmanagementapi.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction save(Transaction transaction) {
        logger.info("Saving transaction: " + transaction.getId());
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction update(Long id, Transaction transactionDetails) {
        // Find the existing transaction by ID
        Transaction existingTransaction = findExistingById(id);

        // Update the fields of the existing transaction
        existingTransaction.setAmount(transactionDetails.getAmount());
        existingTransaction.setIncome(transactionDetails.isIncome());
        
        // If the category or user has changed, update those associations as well
        if (transactionDetails.getCategory() != null) {
            existingTransaction.setCategory(transactionDetails.getCategory());
        }
        if (transactionDetails.getUser() != null) {
            existingTransaction.setUser(transactionDetails.getUser());
        }

        // Save the updated transaction
        return transactionRepository.save(existingTransaction);
    }


    @Override
    public Optional<Transaction> findById(Long id) throws NoSuchElementException {
        return transactionRepository.findById(id);
    }

    public Transaction findExistingById(Long id) {
        return transactionRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Transaction with ID %d not found", id)));
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transaction = new ArrayList<>();
        transactionRepository.findAll().forEach(transaction::add);
        return transaction;
    }

    @Override
    @Transactional
    // Use transactional to ensure the task is closed after completion
    public void delete(Long id) {
        logger.info("Deleting transaction: " + id);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Transaction with id " + id + " not found"));
                transactionRepository.delete(transaction);
        logger.info("Deleted transaction with id deleted successfully" + id);
    }

}
