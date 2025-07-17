package com.example.qbankapi.exception.base.impl;

import com.example.qbankapi.exception.base.EntityAlreadyExistsException;

public class ParticipantUserExamSubmissionAlreadyExistsException extends EntityAlreadyExistsException {

    public ParticipantUserExamSubmissionAlreadyExistsException() {
        super("Participant user exam submission already exists");
    }

    public ParticipantUserExamSubmissionAlreadyExistsException(String message) {
        super(message);
    }

    public ParticipantUserExamSubmissionAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
