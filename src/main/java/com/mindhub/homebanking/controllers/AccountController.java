package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.toList;

@RestController
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;


    @RequestMapping ("api/accounts")
    public List<AccountDTO> getAccount() {
        return accountRepository.findAll()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(toList());
    }

    @RequestMapping("api/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        Optional<Account> optionalAccount = accountRepository.findById(id);
        return optionalAccount.map(account -> new AccountDTO(account)).orElse(null);
    }

    @RequestMapping(path = "api/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication){
        // Obtener el cliente autenticado
        Client currentClient = clientRepository.findByEmail(authentication.getName());

        if (currentClient.getAccounts().size() >= 3) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have alredy 3 accounts");
        }

        String accountNumber;
        do {
        int randomNumber = (int) (Math.random() * 100000000);
        accountNumber = "VIN" + String.format("%08d", randomNumber);
    } while (accountRepository.findByNumber(accountNumber) != null);

        Account newAccount = new Account(accountNumber, LocalDateTime.now(),0);
        currentClient.addAccount(newAccount);
        accountRepository.save(newAccount);
        return new ResponseEntity<>("Created a new account!", HttpStatus.CREATED);
    }
    /*@RequestMapping("/api/accounts")
    public List<AccountDTO> getCurrentClientAccount(Authentication authentication) {
        return accountRepository.findAll()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(toList());
    }*/

    }
