package com.swedbank.backend;

import com.swedbank.backend.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping("/{accountId}/credit")
    public ResponseEntity<TransactionResponseDTO> credit(@PathVariable("accountId") UUID accountId, @RequestBody TransactionRequestDTO transactionRequest) {
        TransactionResponseDTO response = bankService.addMoney(accountId, transactionRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{accountId}/debit")
    public ResponseEntity<TransactionResponseDTO> debit(@PathVariable("accountId") UUID accountId, @RequestBody TransactionRequestDTO transactionRequest) {
        TransactionResponseDTO response = bankService.debitMoney(accountId, transactionRequest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BalanceResponseDTO> balance(@PathVariable("accountId") UUID accountId) {
        BalanceResponseDTO response = bankService.getBalance(accountId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/exchange")
    public ResponseEntity<CurrencyExchangeResponseDTO> exchange(@RequestBody CurrencyExchangeRequestDTO currencyExchangeRequest) {
        CurrencyExchangeResponseDTO response = bankService.currencyExchange(currencyExchangeRequest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}/history")
    public ResponseEntity<PaginationResponse<TransactionOverviewResponseDTO>> transactionHistory(@PathVariable("accountId") UUID accountId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<TransactionOverviewResponseDTO> response = bankService.transactionHistory(accountId, page, size);

        return ResponseEntity.ok(response);
    }
}
