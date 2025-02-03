package ca.vanier.budgetmanagementapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.vanier.budgetmanagementapi.Exceptions.CategoryNotFoundException;
import ca.vanier.budgetmanagementapi.Exceptions.TransactionNotFoundException;
import ca.vanier.budgetmanagementapi.Exceptions.UserNotFoundException;
import ca.vanier.budgetmanagementapi.entity.Category;
import ca.vanier.budgetmanagementapi.entity.Transaction;
import ca.vanier.budgetmanagementapi.entity.UserCategory;
import ca.vanier.budgetmanagementapi.entity.Users;
import ca.vanier.budgetmanagementapi.repository.CategoryRepository;
import ca.vanier.budgetmanagementapi.repository.TransactionRepository;
import ca.vanier.budgetmanagementapi.repository.UserCategoryRepository;
import ca.vanier.budgetmanagementapi.repository.UserRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final UserCategoryRepository userCategoryRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,
            UserRepository userRepository, CategoryRepository categoryRepository,
            UserCategoryRepository userCategoryRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.userCategoryRepository = userCategoryRepository;
    }

    @Override
    @Transactional
    public Transaction save(Transaction transaction) {
        logger.info("Saving transaction: " + transaction.getId());
        Users existingUser = userRepository.findById(transaction.getUser().getId())
        .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + transaction.getUser().getId()));

        // Ensure category exists
        Category existingCategory = categoryRepository.findById(transaction.getCategory().getId())
        .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + transaction.getCategory().getId()));

        // Save the transaction first
        if(transaction.getCategory().getId() == 1){
            transaction.setIncome(true);
        }

        Transaction savedTransaction = transactionRepository.save(transaction);


        if (transactionRepository.existsById(savedTransaction.getId())) {
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
        logger.info("Updating transaction: " + id);

        // Fetch the existing transaction
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with ID: " + id));

        // Check if the category is 1 or description is "Income" and set isIncome to true
        if (transactionDetails.getCategory() != null &&
                (transactionDetails.getCategory().getId() == 1)) {
            existingTransaction.setIncome(true);  // Set isIncome to true if condition is met
        }

        // Update amount if it differs
        if (transactionDetails.getAmount() != existingTransaction.getAmount()) {
            existingTransaction.setAmount(transactionDetails.getAmount());
        }

        // Update category if it differs and is not null
        if (transactionDetails.getCategory() != null &&
                !transactionDetails.getCategory().equals(existingTransaction.getCategory())) {
            existingTransaction.setCategory(transactionDetails.getCategory());
        }

        // Save and return the updated transaction
        return transactionRepository.save(existingTransaction);
    }


    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction findExistingByTransactionId(Long id) {
        return transactionRepository
                .findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(
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
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with id " + id + " not found"));
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
    public List<Transaction> getTransactionsByDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findAllByUserIdAndCreatedAtBetween(userId, startDate, endDate);
    }

}
