package com.swedbank.backend.dto;

import com.swedbank.backend.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionOverviewResponseDTO(UUID transactionId, BigDecimal amount, BigDecimal balanceAfter, TransactionType type, LocalDateTime date) {}
