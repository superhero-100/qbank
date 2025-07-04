package com.example.qbankapi.exception.base.impl;

import com.example.qbankapi.exception.base.EntityAlreadyExistsException;

public class SubjectAlreadyExistsException extends EntityAlreadyExistsException {

    public SubjectAlreadyExistsException() {
        super("Subject already exists");
    }

    public SubjectAlreadyExistsException(String message) {
        super(message);
    }

    public SubjectAlreadyExistsException(Throwable cause) {
        super(cause);
    }

}
