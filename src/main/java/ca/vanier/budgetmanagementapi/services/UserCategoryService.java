package ca.vanier.budgetmanagementapi.services;


public interface UserCategoryService {

    void addUserToCategory(Long userId, Long categoryId);

    void removeUserFromCategory(Long userId, Long categoryId);
}
