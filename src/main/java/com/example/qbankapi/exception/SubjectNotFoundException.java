package com.example.qbankapi.exception;

import com.example.qbankapi.exception.base.EntityNotFoundException;

public class SubjectNotFoundException extends EntityNotFoundException {

    public SubjectNotFoundException() {
        super();
    }

    public SubjectNotFoundException(String message) {
        super("Subject not found");
    }

    public SubjectNotFoundException(Throwable cause) {
        super(cause);
    }
}
