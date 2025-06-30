package com.example.qbankapi.exception;

import com.example.qbankapi.exception.base.EntityNotFoundException;

public class AdminNotFoundException extends EntityNotFoundException {

    public AdminNotFoundException() {
        super();
    }

    public AdminNotFoundException(String message) {
        super("Admin not found");
    }

    public AdminNotFoundException(Throwable cause) {
        super(cause);
    }

}
