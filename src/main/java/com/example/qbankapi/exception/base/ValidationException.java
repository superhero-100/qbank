package com.example.qbankapi.exception.base;

import com.example.qbankapi.exception.AppRuntimeException;

public class ValidationException extends AppRuntimeException {

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

}
