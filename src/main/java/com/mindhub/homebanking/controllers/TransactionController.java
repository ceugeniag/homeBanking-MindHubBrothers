package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.PDFGeneratorService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
public class TransactionController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PDFGeneratorService pdfGeneratorService;


    @Transactional
    @PostMapping(path = "api/transactions")
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

        Account accountFrom = accountService.findByNumber(numberAccountFrom);
        if (accountFrom == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        Account accountTo = accountService.findByNumber(numberAccountTo);
        if (accountTo == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        Double accountFromBalance = accountFrom.getBalance();
        Double accountToBalance = accountTo.getBalance();

        if (accountFromBalance.compareTo(amount) < 0) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }

        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountTo.setBalance(accountTo.getBalance() + amount);

        Transaction transactionDebit = new Transaction();
        transactionDebit.setType(TransactionType.DEBIT);
        transactionDebit.setDescription(description + " - " + numberAccountFrom);
        transactionDebit.setTransactionDate(LocalDateTime.now());
        transactionDebit.setAmount(-amount);
        transactionDebit.setAccount(accountFrom);
        transactionDebit.setBalance(accountFrom.getBalance());

        Transaction transactionCredit = new Transaction();
        transactionCredit.setType(TransactionType.CREDIT);
        transactionCredit.setDescription(description + " - " + numberAccountTo);
        transactionCredit.setTransactionDate(LocalDateTime.now());
        transactionCredit.setAmount(amount);
        transactionCredit.setAccount(accountTo);
        transactionCredit.setBalance(accountTo.getBalance());


        transactionService.saveTransaction(transactionCredit);
        accountService.saveAccount(accountFrom);
        accountService.saveAccount(accountTo);

        return new ResponseEntity<>("Created!", HttpStatus.CREATED);
    }
    @GetMapping("/api/transactions")
    public ResponseEntity<Object> getTransactionsByDate(HttpServletResponse response , Authentication authentication, @RequestParam String accountNumber, @RequestParam String start, @RequestParam String end) throws IOException {
        Client currentClient = clientService.findByEmail(authentication.getName());
        Account account = accountService.findByNumber(accountNumber);
        List<Transaction> transactions;

        if (currentClient == null) {
            return new ResponseEntity<>("Client doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (account == null) {
            return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (!currentClient.getAccounts().contains(account)) {
            return new ResponseEntity<>("Account doesn't belong to this client", HttpStatus.FORBIDDEN);
        }
        if(start.equals("all") || end.equals("all") || start.isEmpty() || end.isEmpty()){
            transactions = transactionService.getTransactionsByAccount(account);
            this.pdfGeneratorService.export(response, transactions, account, "all", "all");
        } else {
            LocalDateTime startDate = LocalDateTime.parse(start);
            LocalDateTime endDate = LocalDateTime.parse(end);
            transactions = transactionService.getTransactionsByAccountAndDate(account, startDate, endDate);

            response.setContentType("application/pdf");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=MB-" + account.getNumber() + "-Transactions.pdf";
            response.setHeader(headerKey, headerValue);
            this.pdfGeneratorService.export(response, transactions, account, start, end);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}