package com.example.qbankapi.exception.base.impl;

import com.example.qbankapi.exception.base.EntityNotFoundException;

public class SubjectNotFoundException extends EntityNotFoundException {

    public SubjectNotFoundException() {
        super("Subject not found");
    }

    public SubjectNotFoundException(String message) {
        super(message);
    }

    public SubjectNotFoundException(Throwable cause) {
        super(cause);
    }

}
