package ca.vanier.budgetmanagementapi.services;


import java.util.List;
import java.util.Optional;

import ca.vanier.budgetmanagementapi.entity.Users;

public interface UserService {

    Users save(Users user);

    Users update(Long id, Users userDetails);

    Optional<Users> findById(Long id);

    List<Users> findAll();

    void delete(Long id);

}