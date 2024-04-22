package com.example.cashcard.repositories;

import com.example.cashcard.domain.CashCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashCardRepository extends CrudRepository<CashCard, Long> {

//    Метод для поиска всех карт пользователя
    Iterable<CashCard> findByOwner(String owner);

}
