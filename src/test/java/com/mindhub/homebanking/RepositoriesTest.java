//package com.mindhub.homebanking;
//
//import com.mindhub.homebanking.models.*;
//import com.mindhub.homebanking.repositories.*;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//import java.util.List;
//import java.util.stream.Collectors;
//import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
//
//
//@SpringBootTest
//@AutoConfigureTestDatabase(replace = NONE)
//public class RepositoriesTest {
//
//    @Autowired
//    LoanRepository loanRepository;
//    @Autowired
//    AccountRepository accountRepository;
//    @Autowired
//    CardRepository cardRepository;
//    @Autowired
//    ClientRepository clientRepository;
//    @Autowired
//    TransactionRepository transactionRepository;
//
//    //Loans
//   @Test
//   public void existLoans(){
//       List<Loan> loans = loanRepository.findAll();
//       assertThat(loans,is(not(empty())));
//    }
//
//    @Test
//    public void existMortgageLoan(){
//        List<Loan>loans= loanRepository.findAll();
//        assertThat(loans, hasItem(hasProperty("name", is("Mortgage"))));
//    }
//
//
//    //Clients
//    @Test
//    public void existClient(){
//        List<Client> clients = clientRepository.findAll();
//        assertThat(clients, hasItem(hasProperty("password",is(not(empty())))));
//   }
//
//    @Test
//    public void existClientPassword(){
//        List<Client> clients = clientRepository.findAll();
//        assertThat(clients, hasItem(hasProperty("password",isA(String.class))));
//   }
//
//
//    //  Accounts
//    @Test
//    public void existAccountBalance(){
//        List<Account> accounts = accountRepository.findAll();
//        assertThat(accounts,hasItem(hasProperty("balance",is(greaterThanOrEqualTo(0.0)))));
//   }
//
//    @Test
//    public void existAccountBalanceDouble(){
//        List<Account> accounts = accountRepository.findAll();
//        assertThat(accounts,hasItem(hasProperty("balance",isA(double.class))));
//   }
//
//    @Test
//    public void existAccountWithoutClient(){
//        List<Account> accounts = accountRepository.findAll();
//        assertThat(accounts,hasItem(hasProperty("client",is(not(empty())))));
//   }
//
//
//    //Transactions
//    @Test
//    public void existTransactions(){
//        List<Transaction>transactions= transactionRepository.findAll();
//        assertThat(transactions, is(not(empty())));
//    }
//
//    @Test
//    public void CreditTransaction(){
//        List<Transaction> transactionsCredit = transactionRepository.findAll().stream().filter( transaction -> transaction.getType() ==  TransactionType.CREDIT).collect(Collectors.toList());
//        assertThat(transactionsCredit, everyItem(hasProperty("amount", greaterThan(0.0))));}
//
//    @Test
//    public void DebitTransaction(){
//        List<Transaction> transactionsDebit = transactionRepository.findAll().stream().filter( transaction -> transaction.getType() ==  TransactionType.DEBIT).collect(Collectors.toList());
//        assertThat(transactionsDebit, everyItem(hasProperty("amount", lessThan(0.0))));}
//
//
//    //  Cards
//    @Test
//    public void existCardWithoutClient(){
//        List<Card> cards = cardRepository.findAll();
//        assertThat(cards,hasItem(hasProperty("client",is(not(empty())))));}
//
//    @Test
//    public void existCardWithIncorrectDate(){
//        List<Card> cards = cardRepository.findAll();
//        assertThat(cards,hasItem(hasProperty("fromDate",is(not(equalTo("thruDate"))))));}
//
//}
