package com.swedbank.backend.dto;

import com.swedbank.backend.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionOverviewResponseDTO(BigDecimal amount, TransactionType type, LocalDateTime date) {}
