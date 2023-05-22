package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
public class CardController {

    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;
    @PostMapping(path = "/api/clients/current/cards")
    public ResponseEntity<Object> createCard(
            @RequestParam CardType cardType,
            @RequestParam ColorType colorType,
            Authentication authentication) {

        // Obtener cliente autenticado
        Client currentClient = clientService.findByEmail(authentication.getName());

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
            existingCard = cardService.findByNumber(cardNumber);
        } while (existingCard != null);

        // Generar CVV aleatorio de 3 dígitos
        int cvv = (int) (Math.random() * 1000);

        // Crear tarjeta con los datos generados
        String cardHolder = currentClient.getFirstName() + " " + currentClient.getLastName();
        Card card = new Card(cardHolder, cardType, colorType, cardNumber, cvv, LocalDateTime.now(), LocalDateTime.now().plusYears(5), currentClient, true);
        cardService.saveCard(card);
        currentClient.addCard(card);
        clientService.saveClient(currentClient);
        return new ResponseEntity<>("Congrats, you created a new card!", HttpStatus.CREATED);
}

//Eliminar tarjetas:
    @PutMapping("api/clients/current/cards") //Este endpoint?
    public ResponseEntity<Object> deleteCards (@RequestParam String number, Authentication authentication){
        //Verifico si el cliente existe
        Client currentClient = clientService.findByEmail(authentication.getName());
        Card card = cardService.findByNumber(number);
        if (card == null){
            return new ResponseEntity<>("This card doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (card.getClient().getId() != (currentClient.getId())){
            return new ResponseEntity<>("This card doesn't belong to you", HttpStatus.FORBIDDEN);
        }
        card.setActive(false);
        cardService.saveCard(card);
        return new ResponseEntity<>("Card deleted successfully", HttpStatus.ACCEPTED);

    }
}
