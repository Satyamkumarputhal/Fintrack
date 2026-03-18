package com.satyam.fintrack.exceptions;

public class UserAlreadyAdminException extends RuntimeException {
    public UserAlreadyAdminException(String message) {
        super(message);
    }
}
