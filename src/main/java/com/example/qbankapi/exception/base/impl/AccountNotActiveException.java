package com.example.qbankapi.exception.base.impl;

import com.example.qbankapi.exception.base.AppRuntimeException;

public class AccountNotActiveException extends AppRuntimeException {

    public AccountNotActiveException() {
        super("Account not active");
    }

    public AccountNotActiveException(String message) {
        super(message);
    }

    public AccountNotActiveException(Throwable cause) {
        super(cause);
    }

}
