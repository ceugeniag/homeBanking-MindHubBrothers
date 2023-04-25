package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private long id;
    private double amount;
    private int payments;
    private String name;
    private long loanId;
    private String loanName; //REVISAR

    //CONSTRUCTOR
    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id= clientLoan.getId();
        this.loanId= clientLoan.getId();
        this.name= clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.loanName= clientLoan.getLoan().getName(); //VER SI ESTA BIEN
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
    public String getName() { return name;}
    public long getLoanId() { return loanId; }

}
