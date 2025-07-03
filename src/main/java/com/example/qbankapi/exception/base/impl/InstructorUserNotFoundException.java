package com.example.qbankapi.exception.base.impl;

import com.example.qbankapi.exception.base.EntityNotFoundException;

public class InstructorUserNotFoundException extends EntityNotFoundException {

    public InstructorUserNotFoundException() {
        super("Instructor user not found");
    }

    public InstructorUserNotFoundException(String message) {
        super(message);
    }

    public InstructorUserNotFoundException(Throwable cause) {
        super(cause);
    }

}
