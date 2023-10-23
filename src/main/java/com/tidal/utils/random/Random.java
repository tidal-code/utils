package com.tidal.utils.random;

public class Random {

    public static int number(int min, int max) {
        java.util.Random r = new java.util.Random();
        return r.nextInt(max - min) + min;
    }

    public static String string(int length) {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxxtz";

        StringBuilder builder = new StringBuilder();
        while (length-- != 0) {
            int character = (int) (Math.random() * letters.length());
            builder.append(letters.charAt(character));
        }
        return builder.toString();
    }
}
