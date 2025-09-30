package com.billing.Invoizo.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component("randomNumberGenerator")
public class RandomNumberGenerator {

    private static final Random rand = new Random();

    public int randomNumber6Digits() {
        return (1 + rand.nextInt(9)) * 100 + rand.nextInt(100000);
    }


    public int randomNumber() {
        return (1 + rand.nextInt(9)) * 10000 + rand.nextInt(10000);
    }

    public int randomNumber3Digits() {
        return (1 + rand.nextInt(9)) * 100 + rand.nextInt(100);
    }

    public int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(999999999);
    }
}
