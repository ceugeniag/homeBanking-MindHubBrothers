package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
@Service
public class LoanServiceImplement implements LoanService {
@Autowired
    private LoanRepository loanRepository;
    @Override
    public List<LoanDTO> getLoans() {
        return loanRepository.findAll()
                .stream()
                .map(loan -> new LoanDTO(loan))
                .collect(toList());
    }
    @Override
    public Loan findById(long id) {
        return loanRepository.findById(id);
    }

    @Override
    public void saveLoan(Loan loan) {loanRepository.save(loan);}

    @Override
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

}
