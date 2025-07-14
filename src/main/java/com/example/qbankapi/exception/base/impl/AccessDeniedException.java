package com.example.qbankapi.exception.base.impl;

import com.example.qbankapi.exception.base.AppRuntimeException;

public class AccessDeniedException extends AppRuntimeException {

    public AccessDeniedException() {
        super("Access denied");
    }

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(Throwable cause) {
        super(cause);
    }

}
