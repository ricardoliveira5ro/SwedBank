package com.swedbank.backend.dto;

import java.math.BigDecimal;

public record CurrencyExchangeResponseDTO(BigDecimal sourceFinalBalance, String sourceCurrency, BigDecimal targetFinalBalance, String targetCurrency) {}
