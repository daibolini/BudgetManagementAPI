package ca.vanier.budgetmanagementapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.vanier.budgetmanagementapi.entity.UserCategory;

@Repository
public interface UserCategoryRepository extends CrudRepository<UserCategory, Long> {}
