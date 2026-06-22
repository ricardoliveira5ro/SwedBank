package com.swedbank.backend.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super("Insufficient Balance. Could not debit the amount " + message);
    }
}
