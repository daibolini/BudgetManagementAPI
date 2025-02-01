package ca.vanier.budgetmanagementapi.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(unique = true, nullable = false)
    private String username;

    //@Column(nullable = false)
    private String password;

    //@Column(nullable = false)
    private String role;

    @CreationTimestamp 
    //@Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp 
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    //@JsonManagedReference
    private List<Transaction> transactions = new ArrayList<>();

    //@ManyToMany(mappedBy = "users")
    //private List<Category> categories;
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    // private List<Category> categories;  // Added user-category relationship

    // Constructors
    public Users() {}

    public Users(String username, String password, String role) {
        this.username = username;
        this.password = hashPassword(password);
        this.role = role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase();
    }

    // Hash password before saving
    private String hashPassword(String plainPassword) {
        return new BCryptPasswordEncoder().encode(plainPassword);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = hashPassword(password);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    // public List<Category> getCategories() {
    //     return categories;
    // }

    // public void setCategories(List<Category> categories) {
    //     this.categories = categories;
    // }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
