package com.swedbank.backend.controller;

import com.swedbank.backend.service.TransactionService;
import com.swedbank.backend.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{accountId}/credit")
    public ResponseEntity<TransactionResponseDTO> credit(@PathVariable("accountId") UUID accountId, @RequestBody TransactionRequestDTO transactionRequest) {
        TransactionResponseDTO response = transactionService.addMoney(accountId, transactionRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{accountId}/debit")
    public ResponseEntity<TransactionResponseDTO> debit(@PathVariable("accountId") UUID accountId, @RequestBody TransactionRequestDTO transactionRequest) {
        TransactionResponseDTO response = transactionService.debitMoney(accountId, transactionRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/exchange")
    public ResponseEntity<CurrencyExchangeResponseDTO> exchange(@RequestBody CurrencyExchangeRequestDTO currencyExchangeRequest) {
        CurrencyExchangeResponseDTO response = transactionService.currencyExchange(currencyExchangeRequest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionOverviewResponseDTO> transaction(@PathVariable("transactionId") UUID transactionId) {
        TransactionOverviewResponseDTO response = transactionService.getTransaction(transactionId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}/history")
    public ResponseEntity<PaginationResponse<TransactionOverviewResponseDTO>> transactionHistory(@PathVariable("accountId") UUID accountId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<TransactionOverviewResponseDTO> response = transactionService.transactionHistory(accountId, page, size);

        return ResponseEntity.ok(response);
    }
}
