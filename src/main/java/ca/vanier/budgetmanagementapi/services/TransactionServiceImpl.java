package ca.vanier.budgetmanagementapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.vanier.budgetmanagementapi.entity.Category;
import ca.vanier.budgetmanagementapi.entity.Transaction;
import ca.vanier.budgetmanagementapi.entity.UserCategory;
import ca.vanier.budgetmanagementapi.entity.Users;
import ca.vanier.budgetmanagementapi.repository.CategoryRepository;
import ca.vanier.budgetmanagementapi.repository.TransactionRepository;
import ca.vanier.budgetmanagementapi.repository.UserCategoryRepository;
import ca.vanier.budgetmanagementapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserCategoryRepository userCategoryRepository;

    @Override
    public Transaction save(Transaction transaction) {
        logger.info("Saving transaction: " + transaction.getId());
        Users existingUser = userRepository.findById(transaction.getUser().getId())
        .orElseThrow(() -> new RuntimeException("User not found"));

        // Ensure category exists
        Category existingCategory = categoryRepository.findById(transaction.getCategory().getId())
        .orElseThrow(() -> new RuntimeException("Category not found"));

        // Save the transaction first
        Transaction savedTransaction = transactionRepository.save(transaction);

        if (transactionRepository.existsById(transaction.getId())) {
            // Create and save a new UserCategory entry
            UserCategory userCategory = new UserCategory();
            userCategory.setUser(existingUser);
            userCategory.setCategory(existingCategory);
            userCategoryRepository.save(userCategory);
        }

        return savedTransaction; 
    }

    @Override
    public Transaction update(Long id, Transaction transactionDetails) {
        //find the existing transaction by ID
        Transaction existingTransaction = findExistingById(id);

        //update the fields of the existing transaction
        existingTransaction.setAmount(transactionDetails.getAmount());
        existingTransaction.setIncome(transactionDetails.isIncome());
        
        // If the category or user has changed, update those associations as well
        if (transactionDetails.getCategory() != null) {
            existingTransaction.setCategory(transactionDetails.getCategory());
        }
        //save the updated transaction
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
    //use transactional to ensure the task is closed after completion
    public void delete(Long id) {
        logger.info("Deleting transaction: " + id);
        Transaction transaction = transactionRepository.findById(id)
                //.orElseThrow(() -> new UserNotFoundException("Transaction with id " + id + " not found"));
                .orElseThrow(() -> new EntityNotFoundException("Transaction with id " + id + " not found"));
                transactionRepository.delete(transaction);
        logger.info("Deleted transaction with id deleted successfully" + id);
    }

    @Override
    public List<Transaction> getAllTransactionsByUserId(Long userId) {
        return transactionRepository.findAllByUserId(userId);
    }

    //This will return expenses 
    //As income is set as category id of 1
    @Override
    public List<Transaction> getExpensesByUserId(Long userId) {
        return transactionRepository.findAllByUserIdAndCategoryIdNot(userId, 1L);
    }

    @Override
    public List<Transaction> getIncomesByUserId(Long userId) {
        return transactionRepository.findAllByUserIdAndCategoryId(userId, 1L);
    }


    @Override
    public double getTotalIncomeByUser(Long userId) {
        return transactionRepository.findAllByUserIdAndCategoryId(userId, 1L)
                .stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    @Override
    public double getTotalExpensesByUser(Long userId) {
        return transactionRepository.findAllByUserIdAndCategoryIdNot(userId, 1L)
                .stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    @Override
    public double getUserBalance(Long userId) {
        double income = getTotalIncomeByUser(userId);
        double expenses = getTotalExpensesByUser(userId);
        return income - expenses; // Balance = Income - Expenses
    }

    // @Override
    // public double getUserBalanceByCategory(Long userId, Long categoryId) {
    //     return transactionRepository.findAllByUserIdAndCategoryIdNot(userId, categoryId)
    //     .stream()
    //     .mapToDouble(Transaction::getAmount)
    //     .sum();
    // }

    @Override
    public Map<String, Double> getUserTransactionCategorySummary(Long userId) {
        return transactionRepository.findAllByUserId(userId)
            .stream()
            .collect(Collectors.groupingBy(
                t -> t.getCategory().getDescription(), // Group by category name
                Collectors.summingDouble(Transaction::getAmount) // Sum the amounts
            ));
    }

    @Override
    public double getUserBalanceByCategory(Long userId, Long categoryId) {
        getUserTransactionCategorySummary(userId);
        
        return getUserBalance(userId);

    }

    @Override
    public List<Transaction> getTransactionsByDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findAllByUserIdAndCreatedAtBetween(userId, startDate, endDate);
    }

}
