package com.example.qbankapi.exception;

import com.example.qbankapi.exception.base.EntityNotFoundException;

public class TeacherNotFoundException extends EntityNotFoundException {

    public TeacherNotFoundException() {
        super("Teacher not found");
    }

    public TeacherNotFoundException(String message) {
        super(message);
    }

    public TeacherNotFoundException(Throwable cause) {
        super(cause);
    }

}
