package com.example.qbankapi.exception.base.impl;

import com.example.qbankapi.exception.base.ValidationException;

public class UsernameAlreadyExistsException extends ValidationException {

    public UsernameAlreadyExistsException() {
        super("Username already exists");
    }

    public UsernameAlreadyExistsException(String message) {
        super(message);
    }

    public UsernameAlreadyExistsException(Throwable cause) {
        super(cause);
    }

}
