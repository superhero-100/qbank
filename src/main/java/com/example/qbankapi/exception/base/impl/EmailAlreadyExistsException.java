package com.example.qbankapi.exception.base.impl;

import com.example.qbankapi.exception.base.ValidationException;

public class EmailAlreadyExistsException extends ValidationException {

    public EmailAlreadyExistsException() {
        super("Email already exists");
    }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    public EmailAlreadyExistsException(Throwable cause) {
        super(cause);
    }

}
