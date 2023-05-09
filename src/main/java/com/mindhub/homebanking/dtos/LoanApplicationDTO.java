package com.mindhub.homebanking.dtos;


import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

public class LoanApplicationDTO {
    private long loanId;
    private double amount;
    private int payments;
    private String numberAccountDestiny;

    //CONSTRUCTOR EN EL QUE VOY A RECIBIR LOS DATOS
    public LoanApplicationDTO(long loanId, double amount, int payments, String numberAccountDestiny) {
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.numberAccountDestiny = numberAccountDestiny;
    }

    //GETTERS
    public long getLoanId() {
        return loanId;
    }
    public double getAmount() {
        return amount;
    }
    public int getPayments() {
        return payments;
    }
    public String getNumberAccountDestiny() {
        return numberAccountDestiny;
    }
}
