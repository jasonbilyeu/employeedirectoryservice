package com.headspring.employeedirectory;

import com.headspring.employeedirectory.db.RandomEmployeeBuilder;

import static com.headspring.employeedirectory.ARandom.aRandom;

public class RandomBuilderSupport {

    public RandomEmployeeBuilder employee() {
        return new RandomEmployeeBuilder();
    }

    public String phoneNumber() {
        return String.format("(%s) %s-%s", aRandom.getNumberText(3), aRandom.getNumberText(3), aRandom.getNumberText(4));
    }
}
