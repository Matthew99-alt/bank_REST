package com.example.bankcards.service;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.dto.TransactionDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.*;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Класс сервис отвечающий за реализацию запросов контроллера и работу с объектами класса Card
 * @see  Card
 * @see  CardRepository
 * @see  CardDTO
 * @see  CardMapper
 * @see  com.example.bankcards.controller.CardController
 */

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Transactional(readOnly = true)
    public List<CardDTO> findAllCards() {
        return cardRepository.findAll()
                .stream()
                .map(cardMapper::makeACardDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CardDTO findCardById(Long encodedId) {
        Card card = cardRepository.findById(encodedId)
                .orElseThrow(() -> new CustomEntityNotFoundException("Указанная карта не найдена"));
        return cardMapper.makeACardDTO(card);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CardDTO saveCard(CardDTO cardDTO) {

            if(cardDTO.getBalance()<0){
                throw new NegativeBalanceException("Balance should be more than zero");
            }

            Card savedCard = cardRepository.save(cardMapper.makeACard(cardDTO));
            return cardMapper.makeACardDTO(savedCard);
    }

    @Transactional
    public CardDTO blockCard(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow();
        card.setStatus(Status.BLOCKED);
        return cardMapper.makeACardDTO(cardRepository.save(card));
    }

    @Transactional
    public CardDTO activateCard(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow();
        card.setStatus(Status.ACTIVE);
        return cardMapper.makeACardDTO(cardRepository.save(card));
    }

    @Transactional
    public CardDTO outdateCard(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow();
        card.setStatus(Status.OUTDATED);
        return cardMapper.makeACardDTO(cardRepository.save(card));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TransactionDTO doTransaction(TransactionDTO transactionDTO){

        Card getFromCard = cardRepository.getReferenceById(transactionDTO.getFromCardId());
        Card getToCard = cardRepository.getReferenceById(transactionDTO.getToCardId());

        if(!Objects.equals(getToCard.getUser().getId(), getFromCard.getUser().getId())){
            throw new DifferentIdentifierException("Id of users of cards are different");
        }

        if(transactionDTO.getAmount()<0){
            throw new NegativeBalanceException("Amount should be more than zero");
        }

        if (getFromCard.getStatus() != Status.ACTIVE || getToCard.getStatus() != Status.ACTIVE) {
            throw new UnactiveCardException("Both cards must be active for transaction");
        }

        if(Objects.equals(getToCard.getId(), getFromCard.getId())){
            throw new SameCardException("The cards for transaction are the same");
        }

        getFromCard.setBalance(getFromCard.getBalance()-transactionDTO.getAmount());
        getToCard.setBalance(getToCard.getBalance()+transactionDTO.getAmount());

        return transactionDTO;
    }

    @Transactional(readOnly = true)
    public List<CardDTO> findByUserId(Long userId){
        return cardRepository.findAll()
                .stream()
                .filter(card -> card.getUser().getId().equals(userId))
                .map(cardMapper::makeACardDTO)
                .toList();
    };

    @Transactional
    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }

    @Transactional
    public CardDTO editCard(CardDTO cardDTO) {
        Card card = cardMapper.makeACard(cardDTO);
        cardRepository.save(card);
        return cardDTO;
    }
}
