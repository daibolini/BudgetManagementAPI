package ca.vanier.budgetmanagementapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ca.vanier.budgetmanagementapi.repository.UserRepository;
import ca.vanier.budgetmanagementapi.entity.Category;
import ca.vanier.budgetmanagementapi.entity.Users;
import ca.vanier.budgetmanagementapi.repository.CategoryRepository;

@Service
public class UserCategoryServiceImpl implements UserCategoryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void addUserToCategory(Long userId, Long categoryId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // if (!user.getCategories().contains(category)) {
        //     user.getCategories().add(category);
        //     userRepository.save(user);
        // }
    }

    @Override
    public void removeUserFromCategory(Long userId, Long categoryId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // user.getCategories().remove(category);
        // userRepository.save(user);
    }
}