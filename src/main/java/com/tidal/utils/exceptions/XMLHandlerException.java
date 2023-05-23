package com.tidal.utils.exceptions;

public class XMLHandlerException extends RuntimeException{
    public XMLHandlerException(){}

    public XMLHandlerException(String message){
        super(message);
    }

    public XMLHandlerException(Throwable cause){
        super(cause);
    }

    public XMLHandlerException(String message, Throwable cause){
        super(message, cause);
    }
}
