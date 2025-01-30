package ca.vanier.budgetmanagementapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.vanier.budgetmanagementapi.services.UserCategoryService;

@RestController
@RequestMapping("/user-categories")
public class UserCategoryController {

    @Autowired
    private UserCategoryService userCategoryService;

    @PostMapping("/add")
    public ResponseEntity<String> addUserToCategory(@RequestParam Long userId, @RequestParam Long categoryId) {
        userCategoryService.addUserToCategory(userId, categoryId);
        return ResponseEntity.ok("User added to category.");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeUserFromCategory(@RequestParam Long userId, @RequestParam Long categoryId) {
        userCategoryService.removeUserFromCategory(userId, categoryId);
        return ResponseEntity.ok("User removed from category.");
    }
}