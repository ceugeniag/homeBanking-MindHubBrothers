package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.ColorType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CardDTO {
    private long id;
    private String cardholder;
    private CardType type;
    private ColorType colorType;
    private String number;
    private int cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;
    private boolean active;

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
        this.active= card.isActive();
    }

    //GETTERS
    public long getId() {return id;}
    public String getCardholder() {return cardholder;}
    public CardType getType() {return type;}
    public ColorType getColorType() {return colorType;}
    public String getNumber() {return number;}
    public int getCvv() {return cvv;}
    public LocalDateTime getFromDate() {return fromDate;}
    public LocalDateTime getThruDate() {return thruDate;}

    public boolean isActive() {
        return active;
    }
}
