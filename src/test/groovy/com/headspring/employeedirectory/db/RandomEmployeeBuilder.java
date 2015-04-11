package com.headspring.employeedirectory.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.headspring.employeedirectory.ARandom.aRandom;

public class RandomEmployeeBuilder extends Employee.EmployeeBuilder {

    public RandomEmployeeBuilder() {
        List<String> phoneNumbers = new ArrayList<>();
        for(int i = 0; i < aRandom.getNumberBetween(1, 3); i++ ) {
            phoneNumbers.add(aRandom.phoneNumber());
        }

        this
            .firstName(aRandom.getFirstName())
            .lastName(aRandom.getLastName())
            .jobTitle(aRandom.getItem(Arrays.asList("Developer", "Product Owner", "Manager", "Scrum Master", "Sys Ops Engineer", "Tester")))
            .location(aRandom.getCity())
            .email(aRandom.getEmailAddress())
            .phoneNumbers(phoneNumbers)
            .employeeType(aRandom.getItem(EmployeeType.values()))
            .password(aRandom.getRandomChars(10));
    }
}
