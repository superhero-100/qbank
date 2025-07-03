package com.example.qbankapi.exception;

public class AppRuntimeException extends RuntimeException {

    public AppRuntimeException() {
        super("App runtime exception");
    }

    public AppRuntimeException(String message) {
        super(message);
    }

    public AppRuntimeException(Throwable cause) {
        super(cause);
    }

}
