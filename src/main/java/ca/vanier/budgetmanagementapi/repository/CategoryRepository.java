package ca.vanier.budgetmanagementapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.vanier.budgetmanagementapi.entity.Category;



//@Repository
//public interface CategoryRepository extends CrudRepository<Category, Long> {}

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {}
