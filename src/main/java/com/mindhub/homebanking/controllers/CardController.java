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

@RestController
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping(path = "/api/clients/current/cards", method = RequestMethod.POST) //CORREGIDO
    public ResponseEntity<Object> createCard(
            @RequestParam CardType cardType,
            @RequestParam ColorType colorType,
            Authentication authentication) {

        // Obtener cliente autenticado
        Client currentClient = clientRepository.findByEmail(authentication.getName());

        // Verificar si el cliente ya tiene 3 tarjetas creadas del tipo a crear
        long count = currentClient.getCards().stream()
                .filter(card -> card.getType() == cardType)
                .count();
        if (count >= 3) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't have more than 3 cards of this type");
        }

        // Verificar si el cliente ya tiene 6 tarjetas creadas en total
        if (currentClient.getCards().size() >= 6) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have already created 6 cards");
        }

        // Verificar si el cliente ya tiene una tarjeta del mismo tipo y color
        if (currentClient.getCards().stream()
                .anyMatch(card -> card.getType() == cardType && card.getColorType() == colorType)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You already have a card of this type and color");
        }

        // Generar número de tarjeta aleatorio
        String cardNumber;
        Card existingCard;
        do {
            int num1 = (int) (Math.random() * 10000);
            int num2 = (int) (Math.random() * 10000);
            int num3 = (int) (Math.random() * 10000);
            int num4 = (int) (Math.random() * 10000);
            cardNumber = String.format("%04d-%04d-%04d-%04d", num1, num2, num3, num4);
            existingCard = cardRepository.findByNumber(cardNumber);
        } while (existingCard != null);

        // Generar CVV aleatorio de 3 dígitos
        int cvv = (int) (Math.random() * 1000);

        // Crear tarjeta con los datos generados
        String cardHolder = currentClient.getFirstName() + " " + currentClient.getLastName();
        Card card = new Card(cardHolder, cardType, colorType, cardNumber, cvv, LocalDate.now(), LocalDate.now().plusYears(5), currentClient);
        cardRepository.save(card);
        currentClient.addCard(card);
        clientRepository.save(currentClient);
        return new ResponseEntity<>("Congrats, you created a new card!", HttpStatus.CREATED);


    /*@RequestMapping(path = "api/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCards(
            @RequestParam CardType cardType,
            @RequestParam ColorType colorType,
            Authentication authentication){

        Client currentClient = clientRepository.findByEmail(authentication.getName());
        Set<Card> cards = currentClient.getCards().stream().filter(card -> card.getType()== cardType).collect(Collectors.toSet());


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
    }*/


}
}
