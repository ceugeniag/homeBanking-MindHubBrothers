package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
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
    private AccountService accountService;
    //private AccountRepository accountRepository;
    @Autowired
    private ClientService clientService;
    //private ClientRepository clientRepository;


    @RequestMapping ("api/clients/current/accounts")
    public List<AccountDTO> getAccount() {
        return accountService.getAccount();
    }

    @RequestMapping("api/clients/current/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.getAccountDTO(id);
    }

    @RequestMapping(path = "api/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication){
        // Obtener el cliente autenticado
        Client currentClient = clientService.findByEmail(authentication.getName());

        if (currentClient.getAccounts().size() >= 3) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have alredy 3 accounts");
        }

        String accountNumber;
        do {
        int randomNumber = (int) (Math.random() * 100000000);
        accountNumber = "VIN" + String.format("%08d", randomNumber);
    } while (accountService.findByNumber() != null);

        Account newAccount = new Account(accountNumber, LocalDateTime.now(),0);
        currentClient.addAccount(newAccount);
        accountService.saveAccount(newAccount);
        return new ResponseEntity<>("Created a new account!", HttpStatus.CREATED);
    }

    }
