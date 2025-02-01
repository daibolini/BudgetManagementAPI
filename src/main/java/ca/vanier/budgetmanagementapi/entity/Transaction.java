package ca.vanier.budgetmanagementapi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    private boolean income; //renamed for better readability

    @CreationTimestamp 
    //@Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp 
    //@Column(nullable = true)
    private LocalDateTime updatedAt;

    //@ManyToOne
    //@JoinColumn(name = "user_id", nullable = false)
    //@JsonBackReference
    //private Users user;

    @ManyToOne
    //@JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isIncome() { // Ensuring proper boolean getter naming
        return income;
    }

    public void setIncome(boolean income) {
        this.income = income;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Removed setCreatedAt() to prevent modifications

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // public Users getUser() {
    //     return user;
    // }

    // public void setUser(Users user) {
    //     this.user = user;
    // }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
