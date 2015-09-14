package com.example.mmataraz.framework.util;

import java.util.Random;

/**
 * Created by Mike on 2/4/2015.
 */
public class RandomNumberGenerator {

    private static Random rand = new Random();

    public static int getRandIntBetween(int lowerBound, int upperBound) {
        return rand.nextInt(upperBound - lowerBound) + lowerBound;
    }

    public static int getRandInt(int upperBound) {
        return rand.nextInt(upperBound);
    }

}
