package com.tidal.utils.exceptions;

public class PendingException extends java.lang.AssertionError {
    public PendingException(String message) {
        super(message);
    }

    public PendingException(Throwable cause) {
        super(cause);
    }

    public PendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
