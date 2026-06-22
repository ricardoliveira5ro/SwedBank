package com.swedbank.backend.controller;

import com.swedbank.backend.dto.AccountResponseDTO;
import com.swedbank.backend.dto.BalanceResponseDTO;
import com.swedbank.backend.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<AccountResponseDTO>> accounts(@PathVariable("userId") UUID userId) {
        List<AccountResponseDTO> response = accountService.getAccounts(userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDTO> account(@PathVariable("accountId") UUID accountId) {
        AccountResponseDTO response = accountService.getAccount(accountId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BalanceResponseDTO> balance(@PathVariable("accountId") UUID accountId) {
        BalanceResponseDTO response = accountService.getBalance(accountId);

        return ResponseEntity.ok(response);
    }
}
