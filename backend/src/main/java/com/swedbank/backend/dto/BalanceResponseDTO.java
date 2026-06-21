package com.swedbank.backend.dto;

import java.math.BigDecimal;

public record BalanceResponseDTO(BigDecimal balance, String currency) {}
