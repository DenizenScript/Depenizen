package com.denizenscript.depenizen.common.util;

import java.util.Random;

public class Utilities {

    private static final Random random = new Random();

    public static int getRandomUnsignedByte() {
        return random.nextInt(256);
    }
}
