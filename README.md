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
### POST	api/users/
To register user.

### GET	    api/users/{id}
Get the user by user id.

### GET	    api/users/
Get the list of users.

### PUT	    api/users/{id}
Update the user by user id.

### DELETE	api/users/{id}
Remove user by user id.

#### Category API
### POST	api/category/
To create a category.

### GET	    api/category/{id}
Get the category by category id.

### GET	    api/category/
Get the list of categories.

### PUT     api/category/{id}
Update the category by category id.

### DELETE	api/category/{id}
Remove category by category id.

#### Transaction API
### POST	api/transaction/
To create a transaction.

### GET	api/transaction/{id}
Get the transaction by transaction id.

### GET		api/transaction/	
Get the list of transactions.

### PUT		api/transaction/{id}
Update the transaction by transaction id.

### DELETE	api/transaction/{id}
Remove transaction by transaction id.
