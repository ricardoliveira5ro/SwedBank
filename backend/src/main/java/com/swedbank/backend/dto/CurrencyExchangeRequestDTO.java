package com.swedbank.backend.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CurrencyExchangeRequestDTO(UUID sourceAccount, UUID targetAccount, BigDecimal amount) {}
