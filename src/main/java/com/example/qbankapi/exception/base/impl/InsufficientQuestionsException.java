package com.example.qbankapi.exception.base.impl;

import com.example.qbankapi.exception.base.AppRuntimeException;

public class InsufficientQuestionsException extends AppRuntimeException {

    public InsufficientQuestionsException() {
        super("InSufficient questions exception");
    }

    public InsufficientQuestionsException(String message) {
        super(message);
    }

    public InsufficientQuestionsException(Throwable cause) {
        super(cause);
    }
}
