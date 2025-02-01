package ca.vanier.budgetmanagementapi.services;

import ca.vanier.budgetmanagementapi.entity.UserCategory;

public interface UserCategoryService {

    void addUserToCategory(Long userId, Long categoryId);

    void removeUserFromCategory(Long userId, Long categoryId);

    UserCategory save(UserCategory userCategory);
}
