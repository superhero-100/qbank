package com.example.qbankapi.exception.base;

public class BaseUserNotFoundException extends EntityNotFoundException {

    public BaseUserNotFoundException() {
        super("Base user not found");
    }

    public BaseUserNotFoundException(String message) {
        super(message);
    }

    public BaseUserNotFoundException(Throwable cause) {
        super(cause);
    }

}
