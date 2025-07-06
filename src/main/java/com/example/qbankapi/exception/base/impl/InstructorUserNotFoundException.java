package com.example.qbankapi.exception.base.impl;

import com.example.qbankapi.exception.base.BaseUserNotFoundException;

public class InstructorUserNotFoundException extends BaseUserNotFoundException {

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
