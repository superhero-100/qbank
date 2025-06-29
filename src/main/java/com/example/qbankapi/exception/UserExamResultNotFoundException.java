package com.example.qbankapi.exception;

import com.example.qbankapi.exception.base.EntityNotFoundException;

public class UserExamResultNotFoundException extends EntityNotFoundException {

    public UserExamResultNotFoundException() {
        super("User exam result not found");
    }

    public UserExamResultNotFoundException(String message) {
        super(message);
    }

    public UserExamResultNotFoundException(Throwable cause) {
        super(cause);
    }
}
