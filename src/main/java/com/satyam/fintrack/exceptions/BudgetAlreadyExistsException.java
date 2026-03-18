package com.satyam.fintrack.exceptions;

public class BudgetAlreadyExistsException extends RuntimeException {

    public BudgetAlreadyExistsException(String message) {
        super(message);
    }
}