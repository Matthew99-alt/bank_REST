package com.example.bankcards.service;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.dto.CardUserDTO;
import com.example.bankcards.dto.TransactionDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardUser;
import com.example.bankcards.exception.*;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.mapper.CardUserMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.CardUserRepository;
import com.example.bankcards.util.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    public List<CardDTO> findAllCards() {
        return cardRepository.findAll()
                .stream()
                .map(cardMapper::makeACardDTO)
                .toList();
    }

    public CardDTO findCardById(Long encodedId) {
        Card card = cardRepository.findById(encodedId)
                .orElseThrow(() -> new CustomEntityNotFoundException("Указанная карта не найдена"));
        return cardMapper.makeACardDTO(card);
    }


    public CardDTO saveCard(CardDTO cardDTO) {

            if(cardDTO.getBalance()<0){
                throw new NegativeBalanceException("Balance should be more than zero");
            }

            Card savedCard = cardRepository.save(cardMapper.makeACard(cardDTO));
            return cardMapper.makeACardDTO(savedCard);
    }

    public CardDTO blockCard(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow();
        card.setStatus(Status.BLOCKED);
        return cardMapper.makeACardDTO(cardRepository.save(card));
    }

    public CardDTO activateCard(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow();
        card.setStatus(Status.ACTIVE);
        return cardMapper.makeACardDTO(cardRepository.save(card));
    }

    public CardDTO outdateCard(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow();
        card.setStatus(Status.OUTDATED);
        return cardMapper.makeACardDTO(cardRepository.save(card));
    }

    @Transactional
    public TransactionDTO doTransaction(TransactionDTO transactionDTO){

        Card getFromCard = cardRepository.getReferenceById(transactionDTO.getFromCardId());
        Card getToCard = cardRepository.getReferenceById(transactionDTO.getToCardId());

        if(getToCard.getUser().getId()!=getFromCard.getUser().getId()){
            throw new DifferentIdentifierException("Id of users of cards are different");
        }

        if(transactionDTO.getAmount()<0){
            throw new NegativeBalanceException("Amount should be more than zero");
        }

        if (getFromCard.getStatus() != Status.ACTIVE || getToCard.getStatus() != Status.ACTIVE) {
            throw new UnactiveCardException("Both cards must be active for transaction");
        }

        if(getToCard.getId() == getFromCard.getId()){
            throw new SameCardException("The cards for transaction are the same");
        }

        getFromCard.setBalance(getFromCard.getBalance()-transactionDTO.getAmount());
        getToCard.setBalance(getToCard.getBalance()+transactionDTO.getAmount());

        return transactionDTO;
    }

    public List<CardDTO> findByUserId(Long userId){
        return cardRepository.findAll()
                .stream()
                .filter(card -> card.getUser().getId().equals(userId))
                .map(cardMapper::makeACardDTO)
                .toList();
    };

    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }


    public CardDTO editCard(CardDTO cardDTO) {
        Card card = cardMapper.makeACard(cardDTO);
        cardRepository.save(card);
        return cardDTO;
    }
}
