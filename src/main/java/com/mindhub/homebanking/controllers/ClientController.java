package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;
    //private ClientRepository clientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountService accountService;
    //private  AccountRepository accountRepository;

    @GetMapping("/api/clients")
    public List<ClientDTO> getClient() {
        return clientService.getClient();
    }
    @GetMapping("api/clients/{id}")
        public ClientDTO getClient(@PathVariable Long id){
            return clientService.getClientDTO(id);
        }
    @PostMapping(path = "api/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientService.findByEmail(email)!=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        String accountNumber;
        do {
            int randomNumber = (int) (Math.random() * 99999999);
            accountNumber = "VIN" + String.format("%08d", randomNumber);
        } while (accountService.findByNumber(accountNumber) != null);

        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account newAccount = new Account(accountNumber, LocalDateTime.now(),0, true, AccountType.SAVINGS);
        clientService.saveClient(newClient);
        newClient.addAccount(newAccount);
        accountService.saveAccount(newAccount);
        return new ResponseEntity<>("Welcome new client!", HttpStatus.CREATED);
    }

    @GetMapping("/api/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        return clientService.getCurrentClient(authentication);
    }


}
