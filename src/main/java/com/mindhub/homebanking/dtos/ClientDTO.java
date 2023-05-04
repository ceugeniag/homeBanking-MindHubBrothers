package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Client;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private long id;
    private String email;
    private String firstName;
    private String lastName;
    public Set<AccountDTO> accounts;
    public Set<ClientLoanDTO> loans;
    public Set<CardDTO> cards;

    //CONSTRUCTOR
    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts= client.getAccountSet()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(Collectors.toSet());
        this.loans= client.getClientLoans()
                .stream()
                .map(clientLoan -> new ClientLoanDTO(clientLoan))
                .collect(Collectors.toSet());
        this.cards = client.getCards()
                .stream()
                .map(card -> new CardDTO(card))
                .collect(Collectors.toSet());
    }

    //GETTERS
    public long getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public Set<AccountDTO> getAccounts() { return accounts;}
    public Set<ClientLoanDTO> getLoans() { return loans;}

}
