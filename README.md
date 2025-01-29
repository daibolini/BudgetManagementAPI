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

#### Customer Registration API
### POST	/customers/register
To register customer.

### GET	/customers/list
Get the list of customers.

### DELETE	/customers/remove/{id}
Remove customer by cusotmer id.
