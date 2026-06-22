package com.swedbank.backend.exception;

public class ExchangeRateNotFoundException extends RuntimeException {
    public ExchangeRateNotFoundException(String fromCurrency, String toCurrency) {
        super(String.format("Exchange rate from %s to %s is not supported", fromCurrency, toCurrency));
    }
}
