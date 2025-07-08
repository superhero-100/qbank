package com.example.qbankapi.exception.base.impl;

import com.example.qbankapi.exception.base.AppRuntimeException;

public class BaseUserAccountNotActiveException extends AppRuntimeException {

    public BaseUserAccountNotActiveException() {
        super("Account not active");
    }

    public BaseUserAccountNotActiveException(String message) {
        super(message);
    }

    public BaseUserAccountNotActiveException(Throwable cause) {
        super(cause);
    }

}
