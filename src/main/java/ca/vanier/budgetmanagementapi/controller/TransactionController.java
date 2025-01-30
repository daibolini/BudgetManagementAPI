package ca.vanier.budgetmanagementapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.vanier.budgetmanagementapi.entity.Transaction;
import ca.vanier.budgetmanagementapi.services.TransactionServiceImpl;

import java.util.List;

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
}
