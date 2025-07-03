package com.example.qbankapi.exception.base;

import com.example.qbankapi.exception.AppRuntimeException;

public class EntityNotFoundException extends AppRuntimeException {

    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }

}
