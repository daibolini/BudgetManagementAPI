package ca.vanier.budgetmanagementapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ca.vanier.budgetmanagementapi.repository.UserRepository;
import ca.vanier.budgetmanagementapi.entity.UserCategory;
import ca.vanier.budgetmanagementapi.repository.CategoryRepository;
import ca.vanier.budgetmanagementapi.repository.UserCategoryRepository;

@Service
public class UserCategoryServiceImpl implements UserCategoryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserCategoryRepository userCategoryRepository;

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

        @Override
        public UserCategory save(UserCategory userCategory) {
                return userCategoryRepository.save(userCategory);
        }
        
}