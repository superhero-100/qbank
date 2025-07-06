package com.example.qbankapi.exception.base.impl;

import com.example.qbankapi.exception.base.BaseUserNotFoundException;

public class ParticipantUserNotFoundException extends BaseUserNotFoundException {

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
