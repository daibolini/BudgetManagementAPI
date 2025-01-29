package ca.vanier.budgetmanagementapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ca.vanier"})
public class BudgetmanagementapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BudgetmanagementapiApplication.class, args);
	}

}
