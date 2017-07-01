package com.krazyhawg.employeedirectory.db;

import com.krazyhawg.employeedirectory.ARandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RandomEmployeeBuilder extends Employee.EmployeeBuilder {

    public RandomEmployeeBuilder() {
        super();
        List<String> phoneNumbers = new ArrayList<>();
        for(int i = 0; i < ARandom.aRandom.getNumberBetween(1, 3); i++ ) {
            phoneNumbers.add(ARandom.aRandom.phoneNumber());
        }

        String firstName = ARandom.aRandom.getFirstName();
        String lastName = ARandom.aRandom.getLastName();

        this
            .firstName(firstName)
            .lastName(lastName)
            .jobTitle(ARandom.aRandom.getItem(Arrays.asList("Developer", "Product Owner", "Manager", "Scrum Master", "Sys Ops Engineer", "Tester")))
            .location(ARandom.aRandom.getCity())
            .email(String.format("%s.%s@blackbaud.com", firstName, lastName))
            .phoneNumbers(phoneNumbers)
            .employeeType(ARandom.aRandom.getItem(EmployeeType.values()))
            .password("password");
    }
}
