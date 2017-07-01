package com.krazyhawg.employeedirectory;

import lombok.experimental.Delegate;
import org.fluttercode.datafactory.impl.DataFactory;

import java.util.Random;

public class ARandom {
    public static final ARandom aRandom = new ARandom();

    @Delegate
    private RandomBuilderSupport randomBuilderSupport;

    @Delegate
    private DataFactory dataFactory;

    @Delegate
    private Random random;

    public ARandom() {
        dataFactory = new DataFactory();
        random = new Random();
        dataFactory.randomize(random.nextInt());
        randomBuilderSupport = new RandomBuilderSupport();
    }
}
