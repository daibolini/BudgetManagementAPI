package ca.vanier.budgetmanagementapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ca.vanier.budgetmanagementapi.repository.UserRepository;
import ca.vanier.budgetmanagementapi.repository.CategoryRepository;

@Service
public class UserCategoryServiceImpl implements UserCategoryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void addUserToCategory(Long userId, Long categoryId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public void removeUserFromCategory(Long userId, Long categoryId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }
}