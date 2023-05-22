package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {
    List<AccountDTO> getAccount();
    AccountDTO getAccountDTO(Long id);

    void saveAccount(Account account);

    Account findByNumber(String number);

    Account findById(long id);
    Account getAccountAuthenticated(String number);
    Account getAccountAuthenticatedDTO(LoanApplicationDTO loanApplicationDTO);
}
