package com.krazyhawg.employeedirectory;

import com.krazyhawg.employeedirectory.db.RandomEmployeeBuilder;

public class RandomBuilderSupport {

    public RandomEmployeeBuilder employee() {
        return new RandomEmployeeBuilder();
    }

    public String phoneNumber() {
        return String.format("(%s) %s-%s", ARandom.aRandom.getNumberText(3), ARandom.aRandom.getNumberText(3), ARandom.aRandom.getNumberText(4));
    }
}
