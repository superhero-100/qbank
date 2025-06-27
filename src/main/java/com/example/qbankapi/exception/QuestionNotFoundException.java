package com.example.qbankapi.exception;

import com.example.qbankapi.exception.base.EntityNotFoundException;

public class QuestionNotFoundException extends EntityNotFoundException {

    public QuestionNotFoundException() {
    }

    public QuestionNotFoundException(String message) {
        super("Subject not found");
    }

    public QuestionNotFoundException(Throwable cause) {
        super(cause);
    }
}
