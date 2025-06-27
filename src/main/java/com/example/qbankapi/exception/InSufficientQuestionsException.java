package com.example.qbankapi.exception;

public class InSufficientQuestionsException extends RuntimeException{

    public InSufficientQuestionsException() {
        super();
    }

    public InSufficientQuestionsException(String message) {
        super(message);
    }

    public InSufficientQuestionsException(Throwable cause) {
        super(cause);
    }
}
