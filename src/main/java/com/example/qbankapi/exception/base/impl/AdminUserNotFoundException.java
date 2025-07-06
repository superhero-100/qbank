package com.example.qbankapi.exception.base.impl;

import com.example.qbankapi.exception.base.BaseUserNotFoundException;

public class AdminUserNotFoundException extends BaseUserNotFoundException {

    public AdminUserNotFoundException() {
        super("Admin user not found");
    }

    public AdminUserNotFoundException(String message) {
        super(message);
    }

    public AdminUserNotFoundException(Throwable cause) {
        super(cause);
    }

}