package com.headspring.employeedirectory.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.headspring.employeedirectory.ARandom.aRandom;

public class RandomEmployeeBuilder extends Employee.EmployeeBuilder {

    public RandomEmployeeBuilder() {
        super();
        List<String> phoneNumbers = new ArrayList<>();
        for(int i = 0; i < aRandom.getNumberBetween(1, 3); i++ ) {
            phoneNumbers.add(aRandom.phoneNumber());
        }

        String firstName = aRandom.getFirstName();
        String lastName = aRandom.getLastName();

        this
            .firstName(firstName)
            .lastName(lastName)
            .jobTitle(aRandom.getItem(Arrays.asList("Developer", "Product Owner", "Manager", "Scrum Master", "Sys Ops Engineer", "Tester")))
            .location(aRandom.getCity())
            .email(String.format("%s.%s@headspring.com", firstName, lastName))
            .phoneNumbers(phoneNumbers)
            .employeeType(aRandom.getItem(EmployeeType.values()))
            .password("password");
    }
}
