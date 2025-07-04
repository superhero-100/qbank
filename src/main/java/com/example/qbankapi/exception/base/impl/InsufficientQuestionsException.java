package com.example.qbankapi.exception.base.impl;

import com.example.qbankapi.exception.base.AppRuntimeException;

public class InSufficientQuestionsException extends AppRuntimeException {

    public InSufficientQuestionsException() {
        super("InSufficient questions exception");
    }

    public InSufficientQuestionsException(String message) {
        super(message);
    }

    public InSufficientQuestionsException(Throwable cause) {
        super(cause);
    }
}
