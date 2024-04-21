package com.example.cashcard.services.impl;

import com.example.cashcard.domain.CashCard;
import com.example.cashcard.repositories.CashCardRepository;
import com.example.cashcard.services.CashCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CashCardServiceImpl implements CashCardService {

    private final CashCardRepository cashCardRepository;

    @Override
    public Optional<CashCard> findById(Long requestedId) {
        return cashCardRepository.findById(requestedId);
    }

    @Override
    public CashCard saveCashCard(CashCard newCashCard) {
        return cashCardRepository.save(newCashCard);
    }

    @Override
    public Iterable<CashCard> findAll() {
        return cashCardRepository.findAll();
    }
}
