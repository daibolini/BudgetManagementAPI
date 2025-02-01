package ca.vanier.budgetmanagementapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.vanier.budgetmanagementapi.Exceptions.UserNotFoundException;
import ca.vanier.budgetmanagementapi.entity.Category;
import ca.vanier.budgetmanagementapi.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category save(Category category) {
        logger.info("Saving category: " + category.getId());
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Long id, Category categoryDetails) {
        Category existingCategory = findExistingById(id);
        //existingCategory.setDescription(existingCategory.getDescription());
        existingCategory.setDescription(categoryDetails.getDescription());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public Optional<Category> findById(Long id) throws NoSuchElementException {
        return categoryRepository.findById(id);
    }

    public Category findExistingById(Long id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Category with ID %d not found", id)));
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        categoryRepository.findAll().forEach(categories::add);
        return categories;
    }

    @Override
    @Transactional
    //use transactional to ensure the task is closed after completion
    public void delete(Long id) {
        logger.info("Deleting category: " + id);
        Category user = categoryRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Category with id " + id + " not found"));
        categoryRepository.delete(user);
        logger.info("Deleted caterogy with id deleted successfully" + id);
    }

}
