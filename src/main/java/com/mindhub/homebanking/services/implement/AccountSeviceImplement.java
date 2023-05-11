package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
@Service
public class AccountSeviceImplement implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<AccountDTO> getAccount() {
        return accountRepository.findAll()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(toList());
    }
    @Override
    public AccountDTO getAccountDTO(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        return optionalAccount.map(account -> new AccountDTO(account)).orElse(null);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account findByNumber() {
        return null;
    }

    @Override
    public Account findByNumber(String accountNumber) {
        return accountRepository.findByNumber(accountNumber);
    }

}
