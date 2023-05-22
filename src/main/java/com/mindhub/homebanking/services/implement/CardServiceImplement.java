package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    private CardRepository cardRepository;
    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }
    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }


   /* @Override
    public List<Card> getCardsNearExpiration(LocalDate localDate) {
        LocalDate currentDate = LocalDate.now();
        LocalDate expiration = currentDate.plusMonths(1); //Si le aviso un mes antes estara bien?
        return cardRepository.findByThruDateBetween(currentDate, expiration);
    }*/


}
