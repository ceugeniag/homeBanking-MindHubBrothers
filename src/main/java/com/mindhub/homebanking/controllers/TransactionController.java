package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RestController
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @Transactional
    @RequestMapping(path = "api/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> makingTransactions(
            @RequestParam Double amount,
            @RequestParam String description,
            @RequestParam String numberAccountFrom,
            @RequestParam String numberAccountTo) {
        if (amount == null || description.isBlank() || numberAccountFrom.isBlank() || numberAccountTo.isBlank()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (numberAccountFrom.equals(numberAccountTo)) {
            return new ResponseEntity<>("These accounts are the same", HttpStatus.FORBIDDEN);
        }

        Account accountFrom = accountRepository.findByNumber(numberAccountFrom);
        if (accountFrom == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        Account accountTo = accountRepository.findByNumber(numberAccountTo);
        if (accountTo == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        Double accountFromBalance = accountFrom.getBalance();
        Double accountToBalance = accountTo.getBalance();

        if (accountFromBalance.compareTo(amount) < 0) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        Transaction transactionDebit = new Transaction();
        transactionDebit.setType(TransactionType.DEBIT);
        transactionDebit.setDescription(description + " - " + numberAccountFrom);
        transactionDebit.setCreationDate(LocalDateTime.now());
        transactionDebit.setAmount(-amount);
        transactionDebit.setAccount(accountFrom);

        Transaction transactionCredit = new Transaction();
        transactionCredit.setType(TransactionType.CREDIT);
        transactionCredit.setDescription(description + " - " + numberAccountTo);
        transactionCredit.setCreationDate(LocalDateTime.now());
        transactionCredit.setAmount(amount);
        transactionCredit.setAccount(accountTo);

        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountTo.setBalance(accountTo.getBalance() + amount);
        transactionRepository.save(transactionDebit);
        transactionRepository.save(transactionCredit);
        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);

        return new ResponseEntity<>("Created!", HttpStatus.CREATED);
    }
}