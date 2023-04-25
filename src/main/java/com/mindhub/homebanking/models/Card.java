package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String cardholder;
    private CardType type;
    private ColorType colorType;
    private String number;
    private double cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;

    //ASOCIACION
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;


    //CONSTRUCTOR
    public Card() {};

    public Card(String cardholder, CardType type, ColorType colorType, String number, double cvv, LocalDate fromDate, LocalDate thruDate, Client client) {
        this.cardholder = cardholder;
        this.type = type;
        this.colorType = colorType;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.client = client;
    }


    //GETTER Y SETTER

    public long getId() { return id;}
    public String getCardholder() {return cardholder; }
    public void setCardholder(String cardholder) { this.cardholder = cardholder;}
    public CardType getType() {return type;}
    public void setType(CardType type) {this.type = type;}
    public ColorType getColorType() {return colorType;}
    public void setColorType(ColorType colorType) {this.colorType = colorType;}
    public String getNumber() {return number;}
    public void setNumber(String number) {this.number = number;}
    public double getCvv() {return cvv;}
    public void setCvv(double cvv) {this.cvv = cvv;}
    public LocalDate getFromDate() {return fromDate;}
    public void setFromDate(LocalDate fromDate) {this.fromDate = fromDate;}
    public LocalDate getThruDate() {return thruDate;}
    public void setThruDate(LocalDate thruDate) {this.thruDate = thruDate;}
    public Client getClient() {return client;}
    public void setClient(Client client) {this.client = client;}

}
