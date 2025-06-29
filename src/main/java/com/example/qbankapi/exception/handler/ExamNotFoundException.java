package com.example.qbankapi.exception.handler;

import com.example.qbankapi.exception.base.EntityNotFoundException;

public class ExamNotFoundException extends EntityNotFoundException {

    public ExamNotFoundException() {
        super();
    }

    public ExamNotFoundException(String message) {
        super("Exam not found");
    }

    public ExamNotFoundException(Throwable cause) {
        super(cause);
    }
}
