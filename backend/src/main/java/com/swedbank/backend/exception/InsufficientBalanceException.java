package com.swedbank.backend.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(String.format("Insufficient Balance. Could not debit the amount %s", message));
    }
}
