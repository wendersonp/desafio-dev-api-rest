package com.wendersonp.account.util;

import java.util.Random;

public class GenerationUtils {

    private GenerationUtils() {}

    private static final Random random = new Random();

    public static String generateAccountNumber() {
        return String.format("%010d", (random.nextLong() * 9_000_000_000L) + 1_000_000_000L);
    }
}
