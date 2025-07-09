package com.example.qbankapi.exception.base.impl;

import com.example.qbankapi.exception.base.AppRuntimeException;

public class InvalidProfileTypeException extends AppRuntimeException {

    public InvalidProfileTypeException() {
        super("Invalid Profile");
    }

    public InvalidProfileTypeException(String message) {
        super(message);
    }

    public InvalidProfileTypeException(Throwable cause) {
        super(cause);
    }
}
