# BudgetManagementAPI

## Description

BudgetManagementAPI is a Spring Boot-based application that provides a RESTful API for tracking expenses and calculating income vs. expense summaries. It utilizes H2 Database for lightweight storage and supports internationalization (i18n) for multilingual support.

The API enables users to record income and expenses via HTTP requests, retrieve transaction histories, and generate financial summaries. This project demonstrates the implementation of a transactional web application with Spring Boot, focusing on efficient expense management, data persistence, and RESTful service integration.

## Getting Started
### Prerequisites
Ensure you have the following installed:
* Java 21 (as specified in the pom.xml file)
* Maven

### Dependencies

This project uses the following dependencies:
* Spring Boot Starter Data JPA
* Spring Boot Starter Web
* Spring Boot DevTools
* H2 Database
* Spring Boot Starter Test
* Spring Security
* JUnit 5 (JUnit Jupiter)
* JUnit 5 API
* JUnit 5 Parameters



## Steps to Run the Project

* Build the project using Maven:
```
mvn clean install
```
* Run the Spring Boot application:
```
mvn spring-boot:run
```
* Access the API at http://localhost:8080.


### Running the Tests
* Run unit and integration tests with Maven:
```
mvn test
```


## API Enpoints

#### User Registration API
UserController contains all components related to user management in the BudgetManagementAPI. It is responsible for handling user creation, retrieval, updates, and deletion through RESTful endpoints.

### POST	api/users/
Creates a new user account with a specified username, password, and role.

### GET	    api/users/{id}
Retrieves details of a specific user by their ID.

### GET	    api/users/
Retrieves a list of all registered users.

### PUT	    api/users/{id}
Updates the user information such as username, password, or role.

### DELETE	api/users/{id}
Deletes a user by their ID.

#### Category API
CategoryController handles all functionalities related to transaction categories in the BudgetManagementAPI. It includes endpoints for creating, updating, retrieving, and deleting transaction categories.

### POST	api/category/
Creates a new transaction category (like Rent, Groceries, etc) for a specified user. Categories are used to classify transactions.

### GET	    api/category/{id}
Retrieves the details of a specific transaction category by its ID. The returned data includes the category description and the associated user.

### GET	    api/category/
Retrieves a list of all transaction categories for all users.

### PUT     api/category/{id}
Updates the description of a transaction category.

### DELETE	api/category/{id}
Deletes a transaction category by its ID.

### GET   api/greetings/greeting
Retrieves a localized greeting message based on the requested language, which is determined through the Accept-Language request header.

#### Transaction API
TransactionController is responsible for handling all transaction related operations in the BudgetManagementAPI. It manages the creation, retrieval, updating, and deletion of income and expense transactions.

### POST	api/transaction/
Records a new income or expense transaction under a specified category and user. The income field determines whether the transaction is categorized as income if true or an expense if false.

### GET	 api/transaction/{id}
Retrieves the details of a specific transaction by its ID.

### GET  api/transaction/	
Retrieves a list of all recorded transactions.

### GET  api/transaction/user/{id}
Retrieves all transactions associated with a specific user ID.

### GET  api/transaction/user/{id}/expenses
Retrieves all expenses associated with a specific user. Filters transactions based on whether they are categorized as expenses.

### GET  api/transaction/user/{id}/incomes
Retrieves all income transactions for a specific user identified by their user ID.

### GET  api/transaction/summary/{id}
Provides a financial summary including total income, total expenses, and the balance for a specific user.

### GET   api/transaction/summary/category/{id}
Provides a summary of a specific transaction category, including total income and expenses for that category.

### GET  api/transaction/1/range?startDate={Date}&endDate={Date}
Retrieves all transactions for a specific user (by user ID) within a given date range. The start and end dates are provided as query parameters to filter transactions occurring within the specified period.

### PUT		api/transaction/{id}
Updates an existing transaction by its ID. The transaction details, including the amount, income/expense type, category, and user, are provided in the request body to modify the existing transaction.

### DELETE	api/transaction/{id}
Deletes a specific transaction identified by its ID. This removes the transaction record from the system until re-running, since hibernate configuration is set to create-drop.
