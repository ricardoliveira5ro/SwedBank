package com.swedbank.backend.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionResponseDTO(UUID accountId, BigDecimal finalBalance, String currency) {}
