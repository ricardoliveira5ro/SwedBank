package com.swedbank.backend;

import com.swedbank.backend.config.WebClientConfig;
import com.swedbank.backend.dto.*;
import com.swedbank.backend.exception.ExchangeRateNotFoundException;
import com.swedbank.backend.exception.InsufficientBalanceException;
import com.swedbank.backend.exception.InvalidAmountException;
import com.swedbank.backend.model.Account;
import com.swedbank.backend.model.ExchangeRate;
import com.swedbank.backend.model.Transaction;
import com.swedbank.backend.model.TransactionType;
import com.swedbank.backend.repository.AccountRepository;
import com.swedbank.backend.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
public class BankService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final WebClientConfig webClientConfig;

    public BankService(AccountRepository accountRepository, TransactionRepository transactionRepository, WebClientConfig webClientConfig) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.webClientConfig = webClientConfig;
    }

    public TransactionResponseDTO addMoney(UUID accountId, TransactionRequestDTO transactionRequest) {
        return performTransaction(TransactionType.CREDIT, accountId, transactionRequest.amount());
    }

    public TransactionResponseDTO debitMoney(UUID accountId, TransactionRequestDTO transactionRequest) {
        return performTransaction(TransactionType.DEBIT, accountId, transactionRequest.amount());
    }

    public BalanceResponseDTO getBalance(UUID accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow();

        return new BalanceResponseDTO(account.getBalance(), account.getCurrency().name());
    }

    public CurrencyExchangeResponseDTO currencyExchange(CurrencyExchangeRequestDTO currencyExchangeRequest) {
        if (currencyExchangeRequest.amount().compareTo(BigDecimal.ZERO) < 0 || currencyExchangeRequest.amount().compareTo(BigDecimal.ZERO) == 0)
            throw new InvalidAmountException();

        Account sourceAccount = accountRepository.findById(currencyExchangeRequest.sourceAccount()).orElseThrow();
        Account targetAccount = accountRepository.findById(currencyExchangeRequest.targetAccount()).orElseThrow();

        if (sourceAccount.getBalance().subtract(currencyExchangeRequest.amount()).compareTo(BigDecimal.ZERO) < 0)
            throw new InsufficientBalanceException(currencyExchangeRequest.amount().toString());

        double exchangeRate;

        try {
            exchangeRate = ExchangeRate.valueOf(sourceAccount.getCurrency().name() + "_TO_" + targetAccount.getCurrency().name()).rate;
        } catch (IllegalArgumentException e) {
            throw new ExchangeRateNotFoundException(sourceAccount.getCurrency().name(), targetAccount.getCurrency().name());
        }

        BigDecimal targetAmount = currencyExchangeRequest.amount().multiply(BigDecimal.valueOf(exchangeRate));

        TransactionResponseDTO sourceResponse = performTransaction(TransactionType.DEBIT, sourceAccount.getId(), currencyExchangeRequest.amount());
        TransactionResponseDTO targetResponse = performTransaction(TransactionType.CREDIT, targetAccount.getId(), targetAmount);

        return new CurrencyExchangeResponseDTO(sourceResponse.finalBalance(), sourceResponse.currency(), targetResponse.finalBalance(), targetResponse.currency());
    }

    public PaginationResponse<TransactionOverviewResponseDTO> transactionHistory(UUID accountId, int page, int size) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Transaction> transactionPage = transactionRepository.findByAccount(account, pageable);

        List<TransactionOverviewResponseDTO> transactionList = transactionRepository.findByAccount(account, pageable)
                .stream()
                .map(transaction -> new TransactionOverviewResponseDTO(transaction.getAmount(), transaction.getType(), transaction.getCreatedAt()))
                .toList();

        return new PaginationResponse<>(transactionList, transactionPage.getNumber(), transactionPage.getSize(), transactionPage.getTotalElements(), transactionPage.getTotalPages());
    }

    // -------------

    private TransactionResponseDTO performTransaction(TransactionType transactionType, UUID accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0 || amount.compareTo(BigDecimal.ZERO) == 0)
            throw new InvalidAmountException();

        Account account = accountRepository.findById(accountId).orElseThrow();

        // External Call
        externalSystemExecution();

        BigDecimal currentBalance = account.getBalance();
        BigDecimal finalBalance;

        if (transactionType == TransactionType.DEBIT) {
            finalBalance = currentBalance.subtract(amount);

            if (finalBalance.compareTo(BigDecimal.ZERO) < 0)
                throw new InsufficientBalanceException(amount.toString());

        } else {
            finalBalance = currentBalance.add(amount);
        }

        account.setBalance(finalBalance.setScale(2, RoundingMode.HALF_EVEN));

        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(transactionType);
        transaction.setBalanceBefore(currentBalance.setScale(2, RoundingMode.HALF_EVEN));
        transaction.setBalanceAfter(finalBalance.setScale(2, RoundingMode.HALF_EVEN));

        transactionRepository.save(transaction);

        return new TransactionResponseDTO(accountId, finalBalance.setScale(2, RoundingMode.HALF_EVEN), account.getCurrency().name());
    }

    private void externalSystemExecution() {
        WebClient webClient = webClientConfig.externalSystemWebClient();
        webClient.get().retrieve().bodyToMono(String.class);
    }
}

