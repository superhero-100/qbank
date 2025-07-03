package com.example.qbankapi.exception.base.impl;

import com.example.qbankapi.exception.base.EntityNotFoundException;

public class ParticipantUserNotFoundException extends EntityNotFoundException {

    public ParticipantUserNotFoundException() {
        super("Participant user not found");
    }

    public ParticipantUserNotFoundException(String message) {
        super(message);
    }

    public ParticipantUserNotFoundException(Throwable cause) {
        super(cause);
    }

}
