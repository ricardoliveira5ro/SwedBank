package com.swedbank.backend.service;

import com.swedbank.backend.dto.AccountResponseDTO;
import com.swedbank.backend.dto.BalanceResponseDTO;
import com.swedbank.backend.model.Account;
import com.swedbank.backend.model.User;
import com.swedbank.backend.repository.AccountRepository;
import com.swedbank.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public List<AccountResponseDTO> getAccounts(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return accountRepository.findByUser(user)
                .stream()
                .map(account -> new AccountResponseDTO(account.getId(), account.getBalance(), account.getCurrency().name()))
                .toList();
    }

    public AccountResponseDTO getAccount(UUID accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow();

        return new AccountResponseDTO(account.getId(), account.getBalance(), account.getCurrency().name());
    }

    public BalanceResponseDTO getBalance(UUID accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow();

        return new BalanceResponseDTO(account.getBalance(), account.getCurrency().name());
    }

}
