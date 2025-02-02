package ca.vanier.budgetmanagementapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.vanier.budgetmanagementapi.Exceptions.TransactionNotFoundException;
import ca.vanier.budgetmanagementapi.entity.Users;
import ca.vanier.budgetmanagementapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public Users save(Users user) {
        logger.info("Saving user: " + user.getId());
        return userRepository.save(user);
    }

    @Override
    public Users update(Long id, Users userDetails) {
        Users existingUser = findExistingById(id);
        existingUser.setUsername(userDetails.getUsername());
        existingUser.setPassword(userDetails.getPassword());
        existingUser.setRole(userDetails.getRole());
        return userRepository.save(existingUser);
    }

    @Override
    public Optional<Users> findById(Long id) throws NoSuchElementException {
        return userRepository.findById(id);
    }

    public Users findExistingById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("User with ID %d not found", id)));
    }

    @Override
    public List<Users> findAll() {
        List<Users> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @Override
    @Transactional
    // Use transactional to ensure the task is closed after completion
    public void delete(Long id) {
        logger.info("Deleting user: " + id);
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("User with id " + id + " not found"));
        userRepository.delete(user);
        logger.info("Deleted user with id deleted successfully" + id);
    }

}
