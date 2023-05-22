package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;

import java.time.LocalDate;
import java.util.List;

public interface CardService {

    Card findByNumber(String number);
    void saveCard (Card card);

   // List<Card> getCardsNearExpiration(LocalDate localDate);

}
