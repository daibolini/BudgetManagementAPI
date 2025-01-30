package ca.vanier.budgetmanagementapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.vanier.budgetmanagementapi.entity.Users;
import ca.vanier.budgetmanagementapi.services.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    // CREATE a new user
    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        Users newUser = userService.save(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // READ (get user by ID)
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        Users user = userService.findExistingById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // READ (get all users)
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // UPDATE a user
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users updatedUser) {
        Users user = userService.update(id, updatedUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // DELETE a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
