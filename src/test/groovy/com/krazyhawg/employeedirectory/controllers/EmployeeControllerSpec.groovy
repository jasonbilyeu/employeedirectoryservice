package com.krazyhawg.employeedirectory.controllers

import com.krazyhawg.employeedirectory.db.Employee
import com.krazyhawg.employeedirectory.Application
import com.krazyhawg.employeedirectory.client.EmployeeClient
import com.krazyhawg.employeedirectory.db.EmployeeRepository
import com.krazyhawg.employeedirectory.db.EmployeeType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.HttpClientErrorException
import spock.lang.Specification
import spock.lang.Unroll

import static com.krazyhawg.employeedirectory.ARandom.aRandom
import static PageConstants.PAGE_NUMBER
import static PageConstants.PAGE_SIZE
import static PageConstants.SEARCH
import static PageConstants.SORT_COLUMN
import static PageConstants.SORT_DIRECTION
import static EmployeeType.HR
import static EmployeeType.REGULAR
import static org.springframework.data.domain.Sort.Direction.DESC
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.FORBIDDEN
import static org.springframework.http.HttpStatus.UNAUTHORIZED
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT

@ContextConfiguration(loader = SpringBootContextLoader.class, classes = Application.class)
@SpringBootTest(webEnvironment = DEFINED_PORT)
class EmployeeControllerSpec extends Specification {

    String hostname = "localhost"

    @Value('${server.port}')
    int port

    @Autowired
    EmployeeRepository employeeRepository

    EmployeeClient employeeClient

    def cleanup() {
        employeeRepository.deleteAll()
    }

    def "should create employee when authenticated as HR employee"() {
        given:
        employeeClient = getEmployeeClientFor(HR)

        when:
        Employee insertedEmployee = employeeClient.create(aRandom.employee().build())
        Employee storedEmployee = employeeRepository.findOne(insertedEmployee.id)

        then:
        storedEmployee == insertedEmployee
        storedEmployee.password == 'password'
    }

    def "should update employee when authenticated as HR employee"() {
        given:
        employeeClient = getEmployeeClientFor(HR)
        Employee employee = employeeRepository.save(aRandom.employee().build())
        employee.firstName = aRandom.firstName

        when:
        Employee updatedEmployee = employeeClient.update(employee)
        Employee storedEmployee = employeeRepository.findOne(updatedEmployee.id)

        then:
        updatedEmployee == employee
        storedEmployee.password == 'password'
    }

    def "should delete employee when authenticated as HR employee"() {
        given:
        employeeClient = getEmployeeClientFor(HR)
        Employee employee = employeeRepository.save(aRandom.employee().build())

        when:
        employeeClient.delete(employee.id)

        then:
        employeeRepository.findOne(employee.id) == null
    }

    def "should get forbidden when attempting to create employee as REGULAR employee"() {
        given:
        employeeClient = getEmployeeClientFor(REGULAR)

        when:
        employeeClient.create(aRandom.employee().build())

        then:
        def ex = thrown(HttpClientErrorException)
        ex.statusCode == FORBIDDEN
    }

    def "should get forbidden when attempting to update employee as REGULAR employee"() {
        given:
        employeeClient = getEmployeeClientFor(REGULAR)
        Employee employee = employeeRepository.save(aRandom.employee().build())
        employee.firstName = aRandom.firstName

        when:
        employeeClient.update(employee)

        then:
        def ex = thrown(HttpClientErrorException)
        ex.statusCode == FORBIDDEN
    }

    def "should get forbidden when attempting to delete employee as REGULAR employee"() {
        given:
        employeeClient = getEmployeeClientFor(REGULAR)
        Employee employee = employeeRepository.save(aRandom.employee().build())

        when:
        employeeClient.delete(employee.id)

        then:
        def ex = thrown(HttpClientErrorException)
        ex.statusCode == FORBIDDEN
    }

    def "should get unauthorized when attempting to create employee when not authenticated"() {
        given:
        employeeClient = new EmployeeClient(hostname, port)

        when:
        employeeClient.create(aRandom.employee().build())

        then:
        def ex = thrown(HttpClientErrorException)
        ex.statusCode == UNAUTHORIZED
    }

    def "should get unauthorized when attempting to update employee when not authenticated"() {
        given:
        employeeClient = new EmployeeClient(hostname, port)

        when:
        employeeClient.update(aRandom.employee().build())

        then:
        def ex = thrown(HttpClientErrorException)
        ex.statusCode == UNAUTHORIZED
    }

    def "should get unauthorized when attempting to delete employee when not authenticated"() {
        given:
        employeeClient = new EmployeeClient(hostname, port)

        when:
        employeeClient.delete(aRandom.nextLong())

        then:
        def ex = thrown(HttpClientErrorException)
        ex.statusCode == UNAUTHORIZED
    }

    def "should find one employee"() {
        given:
        employeeClient = getEmployeeClientFor(REGULAR)
        Employee employee = employeeRepository.save(aRandom.employee().build())

        expect:
        employeeClient.findOne(employee.id) == employee
    }

    def "should get unauthorized when attempting to find one employee when not authenticated"() {
        given:
        employeeClient = new EmployeeClient(hostname, port)

        when:
        employeeClient.findOne(aRandom.nextLong())

        then:
        def ex = thrown(HttpClientErrorException)
        ex.statusCode == UNAUTHORIZED
    }

    @Unroll
    def "should get bad request when attempting to create employee with #scenario"() {
        given:
        employeeClient = getEmployeeClientFor(HR)

        when:
        employeeClient.create(employee)

        then:
        def ex = thrown(HttpClientErrorException)
        ex.statusCode == BAD_REQUEST

        where:
        scenario             | employee
        'null firstName'     | aRandom.employee().firstName(null).build()
        'null lastName'      | aRandom.employee().lastName(null).build()
        'invalid email'      | aRandom.employee().email(aRandom.getRandomText(10)).build()
        'null email'         | aRandom.employee().email(null).build()
        'empty phoneNumbers' | aRandom.employee().phoneNumbers([]).build()
        'null employeeType'  | aRandom.employee().employeeType(null).build()
    }

    def "should find many employees"() {
        given:
        employeeClient = getEmployeeClientFor(REGULAR)
        Employee employee1 = employeeRepository.save(aRandom.employee().build())
        Employee employee2 = employeeRepository.save(aRandom.employee().build())

        expect:
        employeeClient.findMany().containsAll([employee1, employee2])
    }

    def "should get unauthorized when attempting to find many employees when not authenticated"() {
        given:
        employeeClient = new EmployeeClient(hostname, port)

        when:
        employeeClient.findMany()

        then:
        def ex = thrown(HttpClientErrorException)
        ex.statusCode == UNAUTHORIZED
    }

    def "should find many employees specifying all paging and sorting params"() {
        given:
        employeeClient = getEmployeeClientFor(REGULAR)
        Employee employee1 = employeeRepository.save(aRandom.employee().firstName("AAA").build())
        Employee employee2 = employeeRepository.save(aRandom.employee().firstName("AAB").build())

        expect:
        employeeClient.findMany([(PAGE_NUMBER): 1, (PAGE_SIZE): 1, (SORT_DIRECTION): DESC, (SORT_COLUMN): "firstName"]) == [employee2]
        employeeClient.findMany([(PAGE_NUMBER): 2, (PAGE_SIZE): 1, (SORT_DIRECTION): DESC, (SORT_COLUMN): "firstName"]) == [employee1]
    }

    def "should find many employees specifying a search parameter"() {
        given:
        employeeClient = getEmployeeClientFor(REGULAR)
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
