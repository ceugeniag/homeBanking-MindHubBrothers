package com.mindhub.homebanking;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
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
	public CommandLineRunner initData(ClientRepository clientrepository, AccountRepository accountRepository) {
		return (args) -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			clientrepository.save(client1);
			Account account1= new Account("VIN001", LocalDateTime.now(), 5000.00);
			client1.addAccount(account1);
			Account account2= new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.00);

			client1.addAccount(account2);
			accountRepository.save(account1);
			accountRepository.save(account2);
			clientrepository.save(client1);
		};
	}
}