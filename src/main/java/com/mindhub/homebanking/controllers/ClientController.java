package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@RestController
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private  AccountRepository accountRepository;

    @RequestMapping("/api/clients")
    public List<ClientDTO> getClient() {
        return clientRepository.findAll()
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(toList());
    }
    @RequestMapping("api/clients/{id}")
        public ClientDTO getClient(@PathVariable Long id){
            Optional<Client> optionalClient = clientRepository.findById(id);
            return optionalClient.map(client -> new ClientDTO(client)).orElse(null);
        }
    @RequestMapping(path = "api/clients", method = RequestMethod.POST)

    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        /*Random random= new Random();
        int number = random.nextInt(999999);*/
        String accountNumber;
        do {
            int randomNumber = (int) (Math.random() * 100000000);
            accountNumber = "VIN" + String.format("%08d", randomNumber);
        } while (accountRepository.findByNumber(accountNumber) != null);

        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account newAccount = new Account(accountNumber, LocalDateTime.now(),0);
        clientRepository.save(newClient);
        newClient.addAccount(newAccount);
        accountRepository.save(newAccount);
        return new ResponseEntity<>("Welcome new client!", HttpStatus.CREATED);
    }

    @RequestMapping("/api/clients/current")
    public ClientDTO getAll(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }


}
