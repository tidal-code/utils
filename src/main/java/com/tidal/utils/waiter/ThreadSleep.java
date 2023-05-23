package com.tidal.utils.waiter;



import com.tidal.utils.exceptions.RuntimeTestException;

import java.time.Duration;

public class ThreadSleep {

    private static final Sleeper sleeper = Sleeper.SYSTEM_SLEEPER;

    private ThreadSleep(){}

    /**
     * Thread sleep in seconds
     * @param seconds seconds to sleep
     */
    public static void forSeconds(int seconds) {
        long millis = Math.multiplyExact(seconds, 1000);
        try {
            sleeper.sleep(Duration.ofMillis(millis));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeTestException(e);
        }
    }

    public static void forMilliS(long millis) {
        try {
            sleeper.sleep(Duration.ofMillis(millis));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeTestException(e);
        }
    }


}
