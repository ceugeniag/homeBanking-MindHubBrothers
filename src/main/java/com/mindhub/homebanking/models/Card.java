package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private int cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;
    private boolean active;

    //ASOCIACION
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;


    //CONSTRUCTOR
    public Card() {};

    public Card(String cardholder, CardType type, ColorType colorType, String number, int cvv, LocalDateTime fromDate, LocalDateTime thruDate, Client client, boolean active) {
        this.cardholder = cardholder;
        this.type = type;
        this.colorType = colorType;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.client = client;
        this.active = active;
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
    public int getCvv() {return cvv;}
    public void setCvv(int cvv) {this.cvv = cvv;}
    public LocalDateTime getFromDate() {return fromDate;}
    public void setFromDate(LocalDateTime fromDate) {this.fromDate = fromDate;}
    public LocalDateTime getThruDate() {return thruDate;}
    public void setThruDate(LocalDateTime thruDate) {this.thruDate = thruDate;}
    public Client getClient() {return client;}
    public void setClient(Client client) {this.client = client;}

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
