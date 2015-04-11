# README #

* To run the application, type ./gradlew bootRun
* To run the tests, type ./gradlew clean check

### What is this repository for? ###

* This repository is a sample service for administering and viewing an Employee Directory, complete with authentication, an Admin(HR) role, and a Basic (Employee) role.
* By default, the application runs on port 8080 and management port is 8081

### Default users ###
* The application comes with an Admin and Basic user (to show authentication/authorization features):
* * ADMIN user credentials - username: admin@headspring.com password: password
* * BASIC user credentials - username: user@headspring.com password: password

### This application exposes the following REST endpoints: ###

* *  GET - http://localhost:8080/employees
* * GET - http://localhost:8080/employees/id
* * POST (only available to ADMIN users) - http://localhost:8080/employees
* * PUT (only available to ADMIN users) - http://localhost:8080/employees/id
* * DELETE (only available to ADMIN users) - http://localhost:8080/employees/id

* The GET call that returns many employees has some additional query parameters (to support paging, sorting, and searching/filtering):

* * GET page number 2 - http://locahost:8080/employees?pageNumber=2
* * GET with pageSize of 25 records - http://localhost:8080/employees?pageSize=25
* * GET with sortDirection DESC - http://localhost:8080/employees?sortDirection=DESC
* * GET with sortColumn of firstName - http://localhost:8080/employees?sortColumn=firstName
* * GET with custom likewise search for lastName (Tim%) - http://localhost:8080/employees?search=lastName:Tim

### Spring Boot Actuator Exposes the following Management Endpoints: ###

* Here are some of the basic ones:
* * GET health checks for the service - http://localhost:8081/health
* * GET environment configuration - http://localhost:8081/env
* * The full list of management endpoints is here:  http://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html