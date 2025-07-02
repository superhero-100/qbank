package com.example.qbankapi.exception;

import com.example.qbankapi.exception.base.EntityNotFoundException;

public class AdminNotFoundException extends EntityNotFoundException {

    public AdminNotFoundException() {
        super("Admin not found");
    }

    public AdminNotFoundException(String message) {
        super(message);
    }

    public AdminNotFoundException(Throwable cause) {
        super(cause);
    }

}
