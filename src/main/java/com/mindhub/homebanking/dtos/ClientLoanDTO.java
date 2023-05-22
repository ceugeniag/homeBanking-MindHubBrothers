package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private long id;
    private double amount;
    private int payments;
    private long loanId;
    private String loanName;
    private double finalAmount;

    //CONSTRUCTOR
    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id= clientLoan.getId();
        this.loanId= clientLoan.getId();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.loanName= clientLoan.getLoan().getName();
        this.finalAmount=clientLoan.getFinalAmount();
    }

    // GETTER
    public long getId() {
        return id;
    }
    public double getAmount() {
        return amount;
    }
    public int getPayments() {
        return payments;
    }
    public String getLoanName() { return loanName;}
    public long getLoanId() { return loanId; }
    public double getFinalAmount() {
        return finalAmount;
    }
}
