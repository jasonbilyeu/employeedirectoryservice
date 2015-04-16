package com.headspring.employeedirectory;


import com.headspring.employeedirectory.db.Employee;
import com.headspring.employeedirectory.db.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import javax.annotation.PostConstruct;
import java.util.Collections;

import static com.headspring.employeedirectory.db.EmployeeType.HR;
import static com.headspring.employeedirectory.db.EmployeeType.REGULAR;

@Configuration
@EnableJpaRepositories
public class PersistenceConfiguration extends JpaRepositoryConfigExtension {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Value("${numberOfSeedEmployees:10}")
    private int seedEmployeeCount;

    @PostConstruct
    public void insertTestEmployees() {
        if (seedEmployeeCount > 0) {
            employeeRepository.save(Employee.builder()
                    .email("admin@headspring.com")
                    .password("password")
                    .employeeType(HR)
                    .firstName("admin")
                    .lastName("user")
                    .jobTitle("HR Specialist")
                    .location("Austin")
                    .phoneNumbers(Collections.singletonList("(512) 555-5555"))
                    .build());
            for (int i = 0; i < seedEmployeeCount; i++) {
                employeeRepository.save(Employee.builder()
                        .email(String.format("user%s@headspring.com", i))
                        .password("password")
                        .employeeType(REGULAR)
                        .firstName("regular")
                        .lastName(String.format("user%s", i))
                        .jobTitle("Engineer")
                        .location("Austin")
                        .phoneNumbers(Collections.singletonList("(512) 666-6666"))
                        .build());
            }
        }
    }
}
