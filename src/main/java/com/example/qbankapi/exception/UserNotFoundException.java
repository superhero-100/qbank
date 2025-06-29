package com.example.qbankapi.exception;

import com.example.qbankapi.exception.base.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super("User not found");
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}
