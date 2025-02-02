package ca.vanier.budgetmanagementapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.vanier.budgetmanagementapi.entity.Transaction;
import ca.vanier.budgetmanagementapi.services.TransactionServiceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    private TransactionServiceImpl TransactionServiceImpl;

    // CREATE a new user
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction newTransaction = TransactionServiceImpl.save(transaction);
        return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
    }

    // READ (get user by ID)
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Transaction transaction = TransactionServiceImpl.findExistingById(id);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    // READ (get all users)
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransaction() {
        List<Transaction> transactions = TransactionServiceImpl.findAll();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // UPDATE a user
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody Transaction updatedTransaction) {
        Transaction transaction = TransactionServiceImpl.update(id, updatedTransaction);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    // DELETE a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        TransactionServiceImpl.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(TransactionServiceImpl.getAllTransactionsByUserId(userId));
    }

    @GetMapping("/user/{userId}/expenses")
    public ResponseEntity<List<Transaction>> getAllExpensesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(TransactionServiceImpl.getExpensesByUserId(userId));
    }

    @GetMapping("/user/{userId}/incomes")
    public ResponseEntity<List<Transaction>> getAllIncomesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(TransactionServiceImpl.getIncomesByUserId(userId));
    }

    @GetMapping("/summary/{userId}")
    public Map<String, Double> getBudgetSummary(@PathVariable Long userId) {
        Map<String, Double> summary = new HashMap<>();
        summary.put("totalIncome", TransactionServiceImpl.getTotalIncomeByUser(userId));
        summary.put("totalExpenses", TransactionServiceImpl.getTotalExpensesByUser(userId));
        summary.put("balance", TransactionServiceImpl.getUserBalance(userId));
        return summary;
    }

    @GetMapping("/summary/category/{userId}")
        public Map<String, String> getBudgetSummaryByCategory(@PathVariable Long userId) {
            Map<String, String> summary = new HashMap<>();


        Map<String, Double> summaryTotal = TransactionServiceImpl.getUserTransactionCategorySummary(userId);
        summaryTotal.forEach((categoryDescription, amount) -> 
            summary.put(categoryDescription, String.format("%.2f", amount)) // Format to 2 decimal places
        );
        summary.put("Balance", String.format("%.2f", TransactionServiceImpl.getUserBalance(userId)));
        return summary;
    }

    @GetMapping("/{userId}/range/{startDate}/{endDate}")
    public List<Transaction> getTransactionsBetweenDataRange(
        @PathVariable Long userId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return TransactionServiceImpl.getTransactionsByDateRange(userId, startDate, endDate);
    }
}
