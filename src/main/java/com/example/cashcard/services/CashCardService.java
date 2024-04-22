package com.example.cashcard.services;

import com.example.cashcard.domain.CashCard;

import java.util.Optional;

public interface CashCardService {
    Optional<CashCard> findById(Long requestedId);

    CashCard saveCashCard(CashCard newCashCard);

    Iterable<CashCard> findAll();

    Iterable<CashCard> findByOwner(String owner);
}
