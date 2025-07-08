package com.example.qbankapi.exception.base.impl;

import com.example.qbankapi.exception.base.EntityNotFoundException;

public class ParticipantUserExamResultNotFoundException extends EntityNotFoundException {

    public ParticipantUserExamResultNotFoundException() {
        super("User exam result not found");
    }

    public ParticipantUserExamResultNotFoundException(String message) {
        super(message);
    }

    public ParticipantUserExamResultNotFoundException(Throwable cause) {
        super(cause);
    }
}
