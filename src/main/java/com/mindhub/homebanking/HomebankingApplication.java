package com.mindhub.homebanking;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
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
		};
	}
}