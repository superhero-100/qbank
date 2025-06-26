package com.example.qbankapi.exception;

import com.example.qbankapi.exception.base.EntityAlreadyExistsException;

public class SubjectAlreadyExistsException extends EntityAlreadyExistsException {

    public SubjectAlreadyExistsException() {
        super();
    }

    public SubjectAlreadyExistsException(String message) {
        super("Subject already exists");
    }

    public SubjectAlreadyExistsException(Throwable cause) {
        super(cause);
    }

}
