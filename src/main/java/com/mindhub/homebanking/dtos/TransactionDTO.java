package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import java.time.LocalDateTime;
import java.util.Set;

public class TransactionDTO {
    private long id;
    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime transactionDate;
    private double balance;
    private LocalDateTime date;
    Set <AccountDTO> accounts;

    //CONSTRUCTOR
    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.transactionDate = transaction.getTransactionDate();
        this.balance = transaction.getBalance();
        this.transactionDate = transaction.getDate();

    }

    //GETTER
    public long getId() {
        return id;
    }
    public TransactionType getType() {
        return type;
    }
    public double getAmount() {
        return amount;
    }
    public String getDescription() {
        return description;
    }
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    public double getBalance() {
        return balance;
    }

    public LocalDateTime getDate() { return date; }
}
