package com.swedbank.backend.exception;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
        super("Invalid amount. Must be greater than 0");
    }
}
