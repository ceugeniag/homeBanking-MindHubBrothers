package com.mindhub.homebanking;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository) {
		return (args) -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
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

			Client client2 = new Client("Diego", "Perez", "diego@mindhub.com");
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

			Client client3 = new Client("Amelia", "Perez", "ame@mindhub.com");
			Account account3= new Account("VIN003", LocalDateTime.now(), 10000.00);
			accountRepository.save(account3);
			client3.addAccount(account3);
			clientRepository.save(client3);

		};
	}
}