package com.tidal.utils.exceptions;

public class DecryptorException extends RuntimeException {
    public DecryptorException(String message) {
        super(message);
    }

    public DecryptorException(Throwable cause) {
        super(cause);
    }

    public DecryptorException(String message, Throwable cause) {
        super(message, cause);
    }
}
