package com.example.qbankapi.exception;

public class AccountNotActiveException extends RuntimeException {

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
