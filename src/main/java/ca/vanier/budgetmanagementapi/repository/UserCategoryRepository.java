package ca.vanier.budgetmanagementapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.vanier.budgetmanagementapi.entity.Users;

@Repository
public interface UserCategoryRepository extends CrudRepository<Users, Long> {}
