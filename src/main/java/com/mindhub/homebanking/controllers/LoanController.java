package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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


    @RequestMapping("/api/loans")
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
        Loan loan = loanService.findById(loanApplicationDTO.getLoanId()); // ??
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
        String numberAccountDestiny = loanApplicationDTO.getNumberAccountDestiny(); //Creo elnumero de cuenta
        Account accountDestiny = accountService.findByNumber(numberAccountDestiny); //lo busco en el repo

        if (!accountDestiny.getClient().equals(currentClient)) {
            return new ResponseEntity<>("This account does not belong to you", HttpStatus.FORBIDDEN);
        }
            if (accountDestiny == null) {
                return new ResponseEntity<>("This account doesn't exist", HttpStatus.FORBIDDEN);
            }
        /*Loan existingLoan = loanRepository.findById(loanApplicationDTO.getLoanId());
        if (existingLoan.getId() == loanApplicationDTO.getLoanId()) {
            return new ResponseEntity<>("You already have a loan with this id", HttpStatus.FORBIDDEN);
        }*/

            double loanAmountWithInterest = loanApplicationDTO.getAmount() * 1.2; //INTERES BASE del 20%
            int numMonthlyPayments = loanApplicationDTO.getPayments(); // Aca guardo la cantidad de cuotas que voy a realizar

            //INTERESES MENSUALES
            double monthlyInterest = 0.02; //INTERES MENSUAL del 2%
            double monthlyPayment = loanAmountWithInterest / numMonthlyPayments * monthlyInterest; //Valor mensual de cuota


            Transaction loanTransactioncredit = new Transaction();
            loanTransactioncredit.setType(TransactionType.CREDIT);
            loanTransactioncredit.setDescription(loan.getName() + " - loan approved");
            loanTransactioncredit.setCreationDate(LocalDateTime.now());
            loanTransactioncredit.setAmount((loanApplicationDTO.getAmount()));
            loanTransactioncredit.setAccount(accountDestiny);
            transactionService.saveTransaction(loanTransactioncredit);
            accountDestiny.setBalance(accountDestiny.getBalance() + loanTransactioncredit.getAmount());
            accountService.saveAccount(accountDestiny);

            ClientLoan clientLoan = new ClientLoan((monthlyPayment*numMonthlyPayments), loanApplicationDTO.getPayments());
            currentClient.addClientLoan(clientLoan);
            loan.addClientLoan(clientLoan);
            clientLoanRepository.save(clientLoan);

        return new ResponseEntity<>("Great! You just acquired a new loan", HttpStatus.CREATED);
    }
}