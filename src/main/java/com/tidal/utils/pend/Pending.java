package com.tidal.utils.pend;


import com.tidal.utils.exceptions.PendingException;

public class Pending {

    private Pending() {
    }

    public static String test() {
        throw new PendingException("This test is not completed yet");
    }

    public static String test(String message) {
        throw new PendingException(message);
    }

    public static String step() {
        throw new PendingException("This step is not implemented yet");
    }

    public static String step(String message) {
        throw new PendingException(message);
    }

    public static String action() {
        throw new PendingException("This user action is not implemented yet");
    }

    public static String action(String message) {
        throw new PendingException(message);
    }

    public static String task() {
        throw new PendingException("This task is not implemented yet");
    }

    public static String task(String message) {
        throw new PendingException(message);
    }

    public static String rule() {
        throw new PendingException("This rule is not implemented yet");
    }

    public static String rule(String message) {
        throw new PendingException(message);
    }

    public static String process() {
        throw new PendingException("This process is not implemented yet");
    }

    public static String process(String message) {
        throw new PendingException(message);
    }

    public static String persona() {
        throw new PendingException("This persona is not implemented yet");
    }

    public static String persona(String message) {
        throw new PendingException(message);
    }


}