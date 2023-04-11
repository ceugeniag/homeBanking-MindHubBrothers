package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity // Usamos para que la clase se guarde en la base de datos, es decir que cree la tabla
public class Client { //La clase la creamos para crear un objeto de tipo Client
    @Id //indica cual va a ser la primary key
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") //genera un valor automaticamente un valor a la primary key
    @GenericGenerator(name = "native", strategy = "native") //con native relacionamos entre los generators
    private long id;
    private String email = "";
    private String firstName = "";
    private String lastName = "";

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER) //Asociacion de uno a muchos
    private Set<Account> accounts = new HashSet<>(); //Crea un espacio en memoria

    public Client(){ }; //Constructor vacío, JPA lo pide.
    public Client(String firstName, String lastName, String email){ //Constructor con parametros
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
    public Set<Account> getAccountSet() {
        return accounts;
    }
    public void addAccount(Account account) { // Metodo que usamos para agregar cuentas a una coleccion de cuentas de clientes
        account.setClient(this);//Metodo de la clase Account que recibe por argumento un this(que hace referencia al objeto que tenemos instanciado en ese momento)
        accounts.add(account);
    }
}
