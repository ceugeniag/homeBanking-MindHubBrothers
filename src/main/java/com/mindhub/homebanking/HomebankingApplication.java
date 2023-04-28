package com.mindhub.homebanking;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEnconder;
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEnconder.encode("melba"));
			clientRepository.save(client1);
			Account account1= new Account("VIN001", LocalDateTime.now(), 5000.00);
			client1.addAccount(account1);
			Account account2= new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.00);

			client1.addAccount(account2);
			accountRepository.save(account1);
			accountRepository.save(account2);
			clientRepository.save(client1);

			Transaction transaction1= new Transaction(TransactionType.CREDIT, 500.00, "Salary",LocalDateTime.now());
			transactionRepository.save(transaction1);
			account1.addTransactions(transaction1);
			accountRepository.save(account1);

			Transaction transaction2= new Transaction(TransactionType.DEBIT,-50.00, "Bakery",LocalDateTime.now());
			transactionRepository.save(transaction2);
			account2.addTransactions(transaction2);
			accountRepository.save(account2);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);

			Transaction transaction3= new Transaction(TransactionType.DEBIT,-150.00, "Hotel",LocalDateTime.now());
			transactionRepository.save(transaction3);
			account2.addTransactions(transaction3);
			accountRepository.save(account2);
			transactionRepository.save(transaction3);

			Transaction transaction4= new Transaction(TransactionType.CREDIT,256.10, "Transfer",LocalDateTime.now());
			transactionRepository.save(transaction4);
			account2.addTransactions(transaction4);
			accountRepository.save(account2);
			transactionRepository.save(transaction4);

			Transaction transaction5= new Transaction(TransactionType.DEBIT,-50.10, "Transfer",LocalDateTime.now());
			transactionRepository.save(transaction5);
			account1.addTransactions(transaction5);
			accountRepository.save(account1);
			transactionRepository.save(transaction4);

			Loan loan1 = new Loan("Hipotecario", 500000, List.of(12,24,36,48,60));
			loanRepository.save(loan1);
			Loan loan2 = new Loan("Personal", 100000, List.of(6, 12, 24));
			loanRepository.save(loan2);
			Loan loan3 = new Loan("Automotriz", 300000, List.of(6, 12, 24, 36));
			loanRepository.save(loan3);

			Client client2 = new Client("Diego", "Perez", "diego@mindhub.com", passwordEnconder.encode("diego"));
			Account account4= new Account("VIN003", LocalDateTime.now(), 5600.00);
			client2.addAccount(account4);
			clientRepository.save(client2);

			ClientLoan clientLoan1 = new ClientLoan(400000, 60);
			clientLoanRepository.save(clientLoan1);
			ClientLoan clientLoan2 = new ClientLoan(50000, 12);
			clientLoanRepository.save(clientLoan2);
			ClientLoan clientLoan3 = new ClientLoan(100000, 24);
			clientLoanRepository.save(clientLoan3);
			ClientLoan clientLoan4 = new ClientLoan(200000, 36);
			clientLoanRepository.save(clientLoan4);
			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);
			clientRepository.save(client1);
			clientRepository.save(client2);

			loan1.addClientLoan(clientLoan1);
			loanRepository.save(loan1);
			clientRepository.save(client1);
			clientLoanRepository.save(clientLoan1);

			loan2.addClientLoan(clientLoan2);
			loanRepository.save(loan2);
			clientRepository.save(client1);
			clientLoanRepository.save(clientLoan2);

			loan2.addClientLoan(clientLoan3);
			loanRepository.save(loan2);
			clientRepository.save(client2);
			clientLoanRepository.save(clientLoan3);

			loan3.addClientLoan(clientLoan4);
			loanRepository.save(loan3);
			clientRepository.save(client2);
			clientLoanRepository.save(clientLoan4);

			Client client3 = new Client("Amelia", "Perez", "amelia@mindhub.com", passwordEnconder.encode("amelia"));
			Account account3= new Account("VIN003", LocalDateTime.now(), 10000.00);
			accountRepository.save(account3);
			client3.addAccount(account3);
			clientRepository.save(client3);

			Card card1 = new Card(client1.getFirstName()+ " " + client1.getLastName(), CardType.DEBIT, ColorType.GOLD, "0001 0001 0001 0001", 111, LocalDate.now(), LocalDate.now().plusYears(5), client1);
			cardRepository.save(card1);
			client1.addCard(card1);
			Card card2 = new Card(client1.getFirstName()+ " " + client1.getLastName(), CardType.CREDIT, ColorType.TITANIUM, "1000 1000 1000 1000", 100, LocalDate.now(), LocalDate.now().plusYears(5), client1);
			cardRepository.save(card2);
			client1.addCard(card2);
			Card card3 = new Card(client2.getFirstName()+ " " + client2.getLastName(), CardType.CREDIT, ColorType.SILVER, "1001 1001 1001 1001", 101, LocalDate.now(), LocalDate.now().plusYears(5), client2);
			cardRepository.save(card3);
			client2.addCard(card3);
			clientRepository.save(client1);
			clientRepository.save(client2);
			Client client4 = new Client("admin", "admin", "admin@admin.com", passwordEnconder.encode("admin"));
			clientRepository.save(client4);

			Account account5 = new Account("VIN006", LocalDateTime.now(), 150);
			client2.addAccount(account5);
			clientRepository.save(client2);
		};
	}
}