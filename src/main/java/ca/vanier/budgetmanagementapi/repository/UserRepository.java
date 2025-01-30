package ca.vanier.budgetmanagementapi.repository;

import org.springframework.data.repository.CrudRepository;

import ca.vanier.budgetmanagementapi.entity.Users;

public interface UserRepository extends CrudRepository<Users, Long> {}