package com.tidal.utils.waiter;



import com.tidal.utils.exceptions.RuntimeTestException;
import com.tidal.utils.exceptions.TimeoutException;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;
import java.util.function.Supplier;

public class Wait<T> {

   public static Duration forDuration;
   public static Duration pollingEvery;

   private Duration duration;
   private Duration pollingInterval;

   private final T input;
   private final Sleeper sleeper;
   private Supplier<String> messageSupplier = () -> null;

   public Wait(T input){
       this.input = input;
       sleeper = Sleeper.SYSTEM_SLEEPER;
   }

   public Wait<T> withMessage(String message){
       this.messageSupplier = () -> message;
       return this;
   }

   public Wait<T> withMessage(Supplier<String> messageSupplier){
       this.messageSupplier = messageSupplier;
       return this;
   }

   public Wait<T> forDuration(Duration timeDuration){
       duration = timeDuration;
       return this;
   }

   public Wait<T> pollingEvery(Duration pollingInterval){
       this.pollingInterval = pollingInterval;
       return this;
   }

    public <V> V until(Function<? super T, V> isTrue) {
        Duration timeout = duration == null ? forDuration : duration;
        Duration interval = pollingInterval == null ? pollingEvery : pollingInterval;

        Clock clock = Clock.systemDefaultZone();

        Instant end = clock.instant().plus(timeout);

        while(true){
            V value = isTrue.apply(input);

            if(value != null && Boolean.class != value.getClass() || Boolean.TRUE.equals(value)){
                return value;
            }
            if(end.isBefore(clock.instant())){
                String message = messageSupplier != null ?
                        messageSupplier.get() : null;

                String timeOutMessage = String.format(
                        "Expected condition failed: %s (tried for %d second(s) with %d milliseconds interval)",
                        message == null ? "waiting for " + isTrue:message,
                        timeout.getSeconds(), interval.toMillis());
                throw new TimeoutException(timeOutMessage);
            }

            try{
                sleeper.sleep(interval);
            } catch(InterruptedException e){
                Thread.currentThread().interrupt();
                throw new RuntimeTestException(e);
            }
        }
    }


}
