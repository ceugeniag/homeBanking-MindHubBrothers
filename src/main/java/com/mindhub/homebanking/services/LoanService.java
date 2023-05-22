package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {

    List<LoanDTO> getLoans();
    Loan findById(long id);
    void saveLoan(Loan loan);
    List<Loan> findAll();
}
