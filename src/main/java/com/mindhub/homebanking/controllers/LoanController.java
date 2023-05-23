package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;

import com.mindhub.homebanking.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;


import static java.util.stream.Collectors.toList;

@RestController
public class LoanController {

    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired

    private LoanService loanService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientLoanService clientLoanService;


    @GetMapping("/api/loans")
    public List<LoanDTO> getLoans() {
        return loanService.getLoans();
    }

    @Transactional
    @RequestMapping(path = "/api/loans", method = RequestMethod.POST)
    public ResponseEntity<String> addLoan(
            @RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){

        if (loanApplicationDTO.getLoanId()<= 0) {
            return new ResponseEntity<>("The id must be more than 0", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getPayments()<= 0){
            return new ResponseEntity<>("The payments must be more than 0", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount()<= 0){
         return new ResponseEntity<>("The amount must be more than 0", HttpStatus.FORBIDDEN);
        }

        Loan loan = loanService.findById(loanApplicationDTO.getLoanId());

        if (loan == null){
         return new ResponseEntity<>("This loan doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()){
         return new ResponseEntity<>("This amount exceeds the maximum amount offered", HttpStatus.FORBIDDEN);
        }

     if (!loan.getPayments().contains(loanApplicationDTO.getPayments())){
         return new ResponseEntity<>("These payments are not available for this loan", HttpStatus.FORBIDDEN);
     }

        Client currentClient = clientService.findByEmail(authentication.getName());
        String numberAccountDestiny = loanApplicationDTO.getNumberAccountDestiny();
        Account accountDestiny = accountService.findByNumber(numberAccountDestiny);
        if (!accountDestiny.getClient().equals(currentClient)) {
            return new ResponseEntity<>("This account does not belong to you", HttpStatus.FORBIDDEN);
        }
            if (accountDestiny == null) {
                return new ResponseEntity<>("This account doesn't exist", HttpStatus.FORBIDDEN);
            }

        accountDestiny.setBalance(accountDestiny.getBalance() + loanApplicationDTO.getAmount());

            Transaction loanTransactioncredit = new Transaction();
            loanTransactioncredit.setType(TransactionType.CREDIT);
            loanTransactioncredit.setDescription(loan.getName() + " - loan approved");
            loanTransactioncredit.setTransactionDate(LocalDateTime.now());
            loanTransactioncredit.setAmount((loanApplicationDTO.getAmount()));
            loanTransactioncredit.setAccount(accountDestiny);
            transactionService.saveTransaction(loanTransactioncredit);
            accountService.saveAccount(accountDestiny);
            loanTransactioncredit.setBalance(accountDestiny.getBalance());

            ClientLoan clientLoan = new ClientLoan( loanApplicationDTO.getAmount(), loanApplicationDTO.getPayments(), loanApplicationDTO.getAmount() * loan.getInterest());
            currentClient.addClientLoan(clientLoan);
            loan.addClientLoan(clientLoan);
            clientLoanRepository.save(clientLoan);

        return new ResponseEntity<>("Great! You just acquired a new loan", HttpStatus.CREATED);
    }

    @PostMapping("api/loans/manager")
    public ResponseEntity<Object> addLoanAdmin(@RequestBody Loan loan){
        if (loan.getName().isBlank()){
            return new ResponseEntity<>("You need to add the name of the loan", HttpStatus.FORBIDDEN);
        }
        if (loan.getPayments().size() <= 0){
            return new ResponseEntity<>("You need to add a value amount of payments", HttpStatus.FORBIDDEN);
        }
        if (loan.getMaxAmount()< 1){
            return new ResponseEntity<>("You have to enter more amount than 1", HttpStatus.FORBIDDEN);
        }
        if (loan.getInterest()<1){
            return new ResponseEntity<>("You have to enter a valid interest", HttpStatus.FORBIDDEN);
        }

        Loan managerLoan = new Loan(loan.getName(), loan.getMaxAmount(), loan.getPayments(), loan.getInterest());
        loanService.saveLoan(managerLoan);

        return new ResponseEntity<>("Great! You just acquired a new loan", HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("api/current/loans")
    public ResponseEntity<Object> loanPayment(Authentication authentication , @RequestParam long idLoan , @RequestParam String account, @RequestParam double amount) {

        Client currentClient = clientService.findByEmail(authentication.getName());
        ClientLoan clientLoan = clientLoanService.getClientLoan(idLoan);
        Loan loan = loanService.findById(idLoan);
        Account accountAuthenticated = accountService.getAccountAuthenticated(account);
        String description = "You made a payment on your "+ clientLoan.getLoan().getName() + "loan";

        if( clientLoan == null ){
            return new ResponseEntity<>("This loan doesn't exist", HttpStatus.FORBIDDEN);
        }
        if( currentClient == null){
            return new ResponseEntity<>("You are not registered as a client", HttpStatus.FORBIDDEN);
        }
        if( clientLoan.getFinalAmount() == 0 ){
            return new ResponseEntity<>("This loan is already paid", HttpStatus.FORBIDDEN);
        }
        if ( account.isBlank() ){
            return new ResponseEntity<>("PLease enter an account", HttpStatus.FORBIDDEN);
        }
        if ( currentClient.getAccounts().stream().filter(accounts -> accounts.getNumber().equalsIgnoreCase(account)).collect(toList()).size() == 0 ){
            return new ResponseEntity<>("This account is not yours.", HttpStatus.FORBIDDEN);}

        if ( amount < 1 ){
            return new ResponseEntity<>("PLease enter an amount bigger than 0", HttpStatus.FORBIDDEN);
        }
        if ( accountAuthenticated.getBalance() < amount ){
            return new ResponseEntity<>("Insufficient balance in your account " + accountAuthenticated.getNumber(), HttpStatus.FORBIDDEN);}

        accountAuthenticated.setBalance( accountAuthenticated.getBalance() - amount );
        clientLoan.setFinalAmount( clientLoan.getFinalAmount() - amount);

        Transaction newTransaction = new Transaction(TransactionType.DEBIT, amount, description , LocalDateTime.now() , accountAuthenticated.getBalance());
        accountAuthenticated.addTransactions(newTransaction);
        transactionService.saveTransaction(newTransaction);

        if ( amount < clientLoan.getFinalAmount() ){
            clientLoan.setPayments(clientLoan.getPayments() - 1 );
        } else {
            clientLoan.setPayments(0);
        }

        return new ResponseEntity<>("Great! You just made a payment on your loan", HttpStatus.CREATED);
    }

}