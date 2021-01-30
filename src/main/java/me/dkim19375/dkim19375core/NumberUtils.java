package me.dkim19375.dkim19375core;

import java.util.Random;

public class NumberUtils {
    private final static Random random = new Random();

    public static long getPercentage(long lower, long max) { return (lower * 100) / max; }

    public static double getPercentageDouble(double lower, double max) { return (lower * 100) / max; }

    public static boolean percentChance(float chance) { return Math.random() * 100 <= chance; }

    public static boolean percentChance(int chance) { return Math.random() * 100 <= chance; }

    public static int getRandomNumber(int minimum, int maximum) { return random.nextInt(maximum - minimum + 1) + minimum; }
}
