package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String email = "";
    private String firstName = "";
    private String lastName = "";


    //ASOCIACION
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER) //Asociacion de uno a muchos
    private Set<Account> accounts = new HashSet<>(); //Crea un espacio en memoria

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private  Set<ClientLoan> clientLoans = new HashSet<>();


    //CONSTRUCTOR
    public Client(){ };
    public Client(String firstName, String lastName, String email){
        this.firstName= firstName;
        this.lastName=lastName;
        this.email=email;
    }


    //GETTER Y SETTER
    public Set<ClientLoan> getLoan() {
        return clientLoans;
    }
    public Set<Account> getAccounts() {
        return accounts;
    }
    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }
    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }
    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
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
    public Set<Account> getAccountSet() {
        return accounts;
    }


    //METODOS
    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }
   public void  addClientLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }


}
