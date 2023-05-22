package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.Date;

@RepositoryRestResource
public interface CardRepository extends JpaRepository <Card, Long> {
    Card findByNumber(String number);
    Card findByThruDateBetween (LocalDate localDate, LocalDate truDate); //Lo que quiero es buscar el proximo a vender, se podra?

}
