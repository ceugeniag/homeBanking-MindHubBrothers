package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Card;

import java.time.LocalDate;
import java.util.List;

public interface CardService {

    Card findByNumber(String number);
    void saveCard (Card card);

    List<CardDTO> getCards();
}
