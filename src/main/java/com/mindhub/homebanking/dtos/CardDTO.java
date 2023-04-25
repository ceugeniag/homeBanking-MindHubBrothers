package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.ColorType;

import java.time.LocalDate;

public class CardDTO {
    private long id;
    private String cardholder;
    private CardType type;
    private ColorType colorType;
    private String number;
    private double cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;

    //CONSTRUCTOR
    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardholder = card.getCardholder();
        this.type = card.getType();
        this.colorType = card.getColorType();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
    }

    //GETTERS
    public long getId() {return id;}
    public String getCardholder() {return cardholder;}
    public CardType getType() {return type;}
    public ColorType getColorType() {return colorType;}
    public String getNumber() {return number;}
    public double getCvv() {return cvv;}
    public LocalDate getFromDate() {return fromDate;}
    public LocalDate getThruDate() {return thruDate;}
}
