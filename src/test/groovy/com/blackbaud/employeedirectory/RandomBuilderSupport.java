package com.blackbaud.employeedirectory;

import com.blackbaud.employeedirectory.db.RandomEmployeeBuilder;

import static com.blackbaud.employeedirectory.ARandom.aRandom;

public class RandomBuilderSupport {

    public RandomEmployeeBuilder employee() {
        return new RandomEmployeeBuilder();
    }

    public String phoneNumber() {
        return String.format("(%s) %s-%s", aRandom.getNumberText(3), aRandom.getNumberText(3), aRandom.getNumberText(4));
    }
}
