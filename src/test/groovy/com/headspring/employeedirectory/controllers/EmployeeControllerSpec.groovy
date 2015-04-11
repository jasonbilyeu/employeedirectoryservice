package com.headspring.employeedirectory.controllers

import com.headspring.employeedirectory.Application
import com.headspring.employeedirectory.client.EmployeeClient
import com.headspring.employeedirectory.db.Employee
import com.headspring.employeedirectory.db.EmployeeRepository
import com.headspring.employeedirectory.db.EmployeeType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.web.client.HttpClientErrorException
import spock.lang.Specification

import static com.headspring.employeedirectory.ARandom.aRandom
import static PageConstants.PAGE_NUMBER
import static PageConstants.PAGE_SIZE
import static PageConstants.SEARCH
import static PageConstants.SORT_COLUMN
import static PageConstants.SORT_DIRECTION
import static com.headspring.employeedirectory.db.EmployeeType.HR
import static com.headspring.employeedirectory.db.EmployeeType.HR
import static com.headspring.employeedirectory.db.EmployeeType.REGULAR
import static org.springframework.data.domain.Sort.Direction.DESC;

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Application.class)
@WebAppConfiguration
@IntegrationTest
class EmployeeControllerSpec extends Specification {

    String hostname = "localhost"

    @Value('${local.server.port}')
    int port;

    @Autowired
    EmployeeRepository employeeRepository

    EmployeeClient employeeClient

    def setup() {
        cleanup()
        employeeClient = getEmployeeClientFor(REGULAR)
    }

    def cleanup() {
        employeeRepository.deleteAll()
    }

    def "should create employee when authenticated as HR employee" () {
        given:
        employeeClient = getEmployeeClientFor(HR)

        when:
        Employee insertedEmployee = employeeClient.create(aRandom.employee().build())

        then:
        employeeRepository.findOne(insertedEmployee.id) == insertedEmployee
    }

    def "should update employee when authenticated as HR employee" () {
        given:
        employeeClient = getEmployeeClientFor(HR)
        Employee employee = employeeRepository.save(aRandom.employee().build())
        employee.firstName = aRandom.firstName

        expect:
        employeeClient.update(employee) == employee
    }

    def "should delete employee when authenticated as HR employee" () {
        given:
        employeeClient = getEmployeeClientFor(HR)
        Employee employee = employeeRepository.save(aRandom.employee().build())

        when:
        employeeClient.delete(employee.id)

        then:
        employeeRepository.findOne(employee.id) == null
    }

    def "should get forbidden when attempting to create employee as REGULAR employee" () {
        given:
        employeeClient = getEmployeeClientFor(REGULAR)

        when:
        employeeClient.create(aRandom.employee().build())

        then:
        def ex = thrown(HttpClientErrorException)
        ex.statusCode == HttpStatus.FORBIDDEN
    }

    def "should get forbidden when attempting to update employee as REGULAR employee" () {
        given:
        employeeClient = getEmployeeClientFor(REGULAR)
        Employee employee = employeeRepository.save(aRandom.employee().build())
        employee.firstName = aRandom.firstName

        when:
        employeeClient.update(employee)

        then:
        def ex = thrown(HttpClientErrorException)
        ex.statusCode == HttpStatus.FORBIDDEN
    }

    def "should get forbidden when attempting to delete employee as REGULAR employee" () {
        given:
        employeeClient = getEmployeeClientFor(REGULAR)
        Employee employee = employeeRepository.save(aRandom.employee().build())

        when:
        employeeClient.delete(employee.id)

        then:
        def ex = thrown(HttpClientErrorException)
        ex.statusCode == HttpStatus.FORBIDDEN
    }

    def "should find one employee" () {
        given:
        Employee employee = employeeRepository.save(aRandom.employee().build())

        expect:
        employeeClient.findOne(employee.id) == employee
    }

    def "should find many employees" () {
        given:
        Employee employee1 = employeeRepository.save(aRandom.employee().build())
        Employee employee2 = employeeRepository.save(aRandom.employee().build())

        expect:
        employeeClient.findMany().containsAll([employee1, employee2])
    }

    def "should find many employees specifying all paging and sorting params" () {
        given:
        Employee employee1 = employeeRepository.save(aRandom.employee().firstName("AAA").build())
        Employee employee2 = employeeRepository.save(aRandom.employee().firstName("AAB").build())

        expect:
        employeeClient.findMany([(PAGE_NUMBER): 1, (PAGE_SIZE): 1, (SORT_DIRECTION): DESC, (SORT_COLUMN): "firstName"]) == [employee2]
        employeeClient.findMany([(PAGE_NUMBER): 2, (PAGE_SIZE): 1, (SORT_DIRECTION): DESC, (SORT_COLUMN): "firstName"]) == [employee1]
    }

    def "should find many employees specifying a search parameter" () {
        given:
        Employee employee1 = employeeRepository.save(aRandom.employee()
                .firstName("Tabitha")
                .employeeType(REGULAR)
                .build())
        Employee employee2 = employeeRepository.save(aRandom.employee()
                .location("Austin, TX")
                .employeeType(REGULAR)
                .build())
        Employee employee3 = employeeRepository.save(aRandom.employee()
                .firstName("Johnathon")
                .phoneNumbers(["1234567890", "0987654321"])
                .employeeType(HR)
                .build())
        Employee employee4 = employeeRepository.save(aRandom.employee()
                .firstName("Tabitha")
                .lastName("LastName")
                .employeeType(HR)
                .build())

        expect:
        employeeClient.findMany([(SEARCH): "firstName:Tab"]).containsAll([employee1, employee4])
        employeeClient.findMany([(SEARCH): "location:Austin"]) == [employee2]
        employeeClient.findMany([(SEARCH): "phoneNumbers:1234567890"]) == [employee3]
        employeeClient.findMany([(SEARCH): "firstName:Tab,lastName:LastName"]) == [employee4]
        employeeClient.findMany([(SEARCH): "employeeType:REGULAR"]).containsAll([employee1, employee2])
        employeeClient.findMany([(SEARCH): "employeeType:HR"]).containsAll([employee3, employee4])
    }

    EmployeeClient getEmployeeClientFor(EmployeeType employeeType) {
        new EmployeeClient(hostname, port).forEmployee(employeeRepository.save(aRandom.employee().employeeType(employeeType).build()))
    }
}
