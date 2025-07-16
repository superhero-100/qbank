package com.example.qbankapi.exception.base.impl;

import com.example.qbankapi.exception.base.EntityNotFoundException;

public class ParticipantUserExamEnrollmentNotFoundException extends EntityNotFoundException {

    public ParticipantUserExamEnrollmentNotFoundException() {
        super("Participant user exam enrollment not found.");
    }

    public ParticipantUserExamEnrollmentNotFoundException(String message) {
        super(message);
    }

    public ParticipantUserExamEnrollmentNotFoundException(Throwable cause) {
        super(cause);
    }
}
