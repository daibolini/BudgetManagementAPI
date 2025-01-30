package ca.vanier.budgetmanagementapi.services;


import java.util.List;
import java.util.Optional;

import ca.vanier.budgetmanagementapi.entity.Category;

public interface CategoryService {

    Category save(Category category);

    Category update(Long id, Category categoryDetails);

    Optional<Category> findById(Long id);

    List<Category> findAll();

    void delete(Long id);

}