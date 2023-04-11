package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private long id;
    private String email = "";
    private String firstName = "";
    private String lastName = "";
    public Set<AccountDTO> accounts;

    public ClientDTO(){ }

    public ClientDTO(Client client) { //Se trabaja con las propiedades de client, las abstraemos
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts= client.getAccountSet()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(Collectors.toSet());
    }
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

}
