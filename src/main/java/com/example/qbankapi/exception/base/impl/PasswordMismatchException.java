package com.example.qbankapi.exception.base.impl;

import com.example.qbankapi.exception.base.ValidationException;

public class PasswordMismatchException extends ValidationException {

    public PasswordMismatchException() {
        super("Password mismatch");
    }

    public PasswordMismatchException(String message) {
        super(message);
    }

    public PasswordMismatchException(Throwable cause) {
        super(cause);
    }
}
