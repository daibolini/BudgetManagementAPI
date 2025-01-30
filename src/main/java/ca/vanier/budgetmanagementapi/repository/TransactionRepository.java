package ca.vanier.budgetmanagementapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.vanier.budgetmanagementapi.entity.Transaction;


@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    

}