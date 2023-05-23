package com.tidal.utils.exceptions;

public class RuntimeTestException extends java.lang.AssertionError {
    public RuntimeTestException(String message) {
        super(message);
    }

    public RuntimeTestException(Throwable cause) {
        super(cause);
    }

    public RuntimeTestException(String message, Throwable cause) {
        super(message, cause);
    }
}
