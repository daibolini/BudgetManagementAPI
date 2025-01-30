package ca.vanier.budgetmanagementapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EntityScan(basePackages = "ca.vanier.budgetmanagementapi.entity") 
public class BudgetmanagementapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BudgetmanagementapiApplication.class, args);
	}

}
