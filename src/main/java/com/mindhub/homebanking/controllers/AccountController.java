package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
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
    @Autowired
    private ClientService clientService;

    @GetMapping ("api/clients/current/accounts")
    public List<AccountDTO> getAccount() {
        return accountService.getAccount();
    }

   /* @GetMapping("api/clients/current/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.getAccountDTO(id);
    }*/

    @PostMapping(path = "api/clients/current/accounts")
    public ResponseEntity<Object> createAccount(@RequestParam AccountType accountType, Authentication authentication){
        // Obtener el cliente autenticado
        Client currentClient = clientService.findByEmail(authentication.getName());
        int activeAccountsCount = 0;


        for (Account account : currentClient.getAccounts()) {
            if (account.isActive()) {
                activeAccountsCount++;
            }
        }
        if (currentClient.getAccounts().size() >= 3) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have alredy 3 accounts");
        }
        String accountNumber;
        do {
        int randomNumber = (int) (Math.random() * 100000000);
        accountNumber = "VIN" + String.format("%08d", randomNumber);
    } while (accountService.findByNumber(accountNumber) != null);

        Account newAccount = new Account(accountNumber, LocalDateTime.now(),0, true, accountType);
        currentClient.addAccount(newAccount);
        accountService.saveAccount(newAccount);
        return new ResponseEntity<>("Created a new account!", HttpStatus.CREATED);
    }

    //Elimino cuentas:
    @PutMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> deleteAccount(@RequestParam long id, Authentication authentication) {
        // Verificando si existe
        Client currentClient = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id);
        if (account == null) {
            return new ResponseEntity<>("This account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (account.getClient().getId() != (currentClient.getId())) {
            return new ResponseEntity<>("This account doesn't belong to you", HttpStatus.FORBIDDEN);
        }
        // Eliminar la cuenta
        account.setActive(false);
        accountService.saveAccount(account);
        return new ResponseEntity<>("Account deleted successfully", HttpStatus.ACCEPTED);
    }
    @GetMapping("/api/clients/current/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication){

        Client currentClient = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id);
        AccountDTO accountDTO = accountService.getAccountDTO(id);

        if ( !currentClient.getAccounts().stream().filter( accountCLient -> accountCLient.getId() == account.getId() ).collect(toList()).isEmpty() ){
            return new ResponseEntity<>(accountDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("This account is not yours", HttpStatus.FORBIDDEN);
        }
    }
}
