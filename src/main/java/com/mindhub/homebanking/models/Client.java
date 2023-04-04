package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {
    @Id //indica cual va a ser la primary key
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") //genera un valor automaticamente un valor a la primary key
    @GenericGenerator(name = "native", strategy = "native") //con native relacionamos entre los generators
    private long id;
    private String email = "";
    private String firstName = "";
    private String lastName = "";

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();

    public Set<Account> getAccountSet() {
        return accounts;
    }

    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

    public Client(){ };
    public Client(String firstName, String lastName, String email){
        this.firstName= firstName;
        this.lastName=lastName;
        this.email=email;

    }
    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
