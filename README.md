# README #

* To run the application, type ./gradlew bootRun
* To run the tests, type ./gradlew clean check

### What is this repository for? ###

* This repository is a sample service for administering and viewing an Employee Directory, complete with authentication, an Administrator (HR) role, and a Basic (Employee) role.
* By default, the application runs on port 8080 and management port is 8081
* This application exposes the following REST endpoints:

* *  GET - http://localhost:8080/employees
* * POST - http://localhost:8080/employees
* * PUT - http://localhost:8080/employees/id
* * GET - http://localhost:8080/employees/id
* * DELETE - http://localhost:8080/employees/id

* The GET call that returns many employees has some additional query parameters (to support paging, sorting, and searching/filtering):

* * GET page number 2 - http://locahost:8080/employees?pageNumber=2
* * GET with pageSize of 25 records - http://localhost:8080/employees?pageSize=25
* * GET with sortDirection DESC - http://localhost:8080/employees?sortDirection=DESC
* * GET with sortColumn of firstName - http://localhost:8080/employees?sortColumn=firstName
* * GET with custom likewise search for lastName (Tim%) - http://localhost:8080/employees?search=lastName:Tim