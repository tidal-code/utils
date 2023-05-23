package com.tidal.utils.waiter;

import java.time.Duration;

public interface Sleeper {

    Sleeper SYSTEM_SLEEPER = d -> Thread.sleep(d.toMillis());

    void sleep(Duration duration) throws InterruptedException;

}
