package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;


    @RequestMapping(path = "api/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCards(
            @RequestParam CardType cardType, @RequestParam ColorType colorType, Authentication authentication){
        Client currentClient = clientRepository.findByEmail(authentication.getName());
        Set<Card> cards = currentClient.getCards().stream().filter(card -> card.getType()== cardType).collect(Collectors.toSet());

//        (cardType == CardType.DEBIT && colorType == colorType.SILVER )
        /*if (currentClient.getCards().size() >= 6){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have alredy 6 cards");
        }
        if ((currentClient.getCards().stream().filter(card -> card.getColorType() == ColorType.SILVER). count() >= 2)||(currentClient.getCards().stream().filter(card -> card.getColorType() == ColorType.GOLD). count() >= 2)||(currentClient.getCards().stream().filter(card -> card.getColorType() == ColorType.TITANIUM). count() >= 2)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have alredy 2 cards of that color");
        }*/
       /* if (currentClient.getCards().stream().filter(card -> card.getColorType() == ColorType.GOLD). count() >= 2){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have alredy 2 Gold cards");
        }*/
       /* if (currentClient.getCards().stream().filter(card -> card.getColorType() == ColorType.TITANIUM). count() >= 2){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have alredy 2 Titanium cards");
        }*/
        /*if ((currentClient.getCards().stream().filter(card -> card.getType() == CardType.DEBIT). count() >= 3)||(currentClient.getCards().stream().filter(card -> card.getType() == CardType.CREDIT). count() >= 3)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have alredy 3 cards");
        }*/
        /*if (currentClient.getCards().stream().filter(card -> card.getType() == CardType.CREDIT). count() >= 3){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have alredy 3 credit cards");
        }*/
        if(cards.size() >=3){
            return new ResponseEntity<>("You cant have more than 3 cards", HttpStatus.FORBIDDEN);
        }
        if (currentClient.getCards().size() >= 6){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have alredy 6 cards");
        }
        if(cards.stream().anyMatch(card -> card.getColorType()== colorType)){
            return new ResponseEntity<>("You cant have same card", HttpStatus.FORBIDDEN);
        }

        Random random = new Random();
        int num1 = random.nextInt(9999);
        int num2 = random.nextInt(9999);
        int num3 = random.nextInt(9999);
        int num4 = random.nextInt(9999);
        String cardNumber = Integer.toString(num1) + " " + Integer.toString(num2) + " "+ Integer.toString(num3) + " "+ Integer.toString(num4) + " ";
        int cvv = random.nextInt(999);

        // ME FALTA DECIR QUE NO SE REPITA EL NUMERO COMPLETO Y QUE SE COMPLETE CON 0,
        // problema para la Euge de mañana

        Card newCard = new Card(currentClient.getFirstName()+ " " + currentClient.getLastName(), cardType, colorType, cardNumber, cvv, LocalDate.now(), LocalDate.now().plusYears(5), currentClient);
        cardRepository.save(newCard);
        currentClient.addCard(newCard);
        clientRepository.save(currentClient);
        return new ResponseEntity<>("Congrats, you created a new card!", HttpStatus.CREATED);
    }


}
