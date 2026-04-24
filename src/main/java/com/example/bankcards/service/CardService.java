package com.example.bankcards.service;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.dto.TransactionDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.*;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.Status;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Класс сервис отвечающий за реализацию запросов контроллера и работу с объектами класса Card
 *
 * @see Card
 * @see CardRepository
 * @see CardDTO
 * @see CardMapper
 * @see com.example.bankcards.controller.CardController
 */
@Slf4j
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
                .orElseThrow(() -> new EntityNotFoundException("Указанная карта не найдена"));
        return cardMapper.makeACardDTO(card);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CardDTO saveCard(CardDTO cardDTO) {
        if (cardDTO.getBalance() < 0) {
            throw new NegativeBalanceException("Недостаточно средств");
        }

        Card savedCard = cardRepository.save(cardMapper.makeACard(cardDTO));
        return cardMapper.makeACardDTO(savedCard);
    }

    @Transactional
    public CardDTO blockCard(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(); // ?
        card.setStatus(Status.BLOCKED);
        return cardMapper.makeACardDTO(cardRepository.save(card));
    }

    @Transactional
    public CardDTO activateCard(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(); // ?
        card.setStatus(Status.ACTIVE);
        return cardMapper.makeACardDTO(cardRepository.save(card));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE) // todo: почитай пока про уровни изоляции в бд
    public TransactionDTO transfer(TransactionDTO transactionDTO/*, UserDetailsImpl userDetails*/) {

        /*if (!cardRepository.getReferenceById(transactionDTO.fromCardId()).getUser().getId().equals(userDetails.getId())){
            throw new DifferentIdentifierException("Введен не верный идентификатор");
        }
        if (!cardRepository.getReferenceById(transactionDTO.toCardId()).getUser().getId().equals(userDetails.getId())){
            throw new DifferentIdentifierException("Введен не верный идентификатор");
        }*/

        Card source = cardRepository.getReferenceById(transactionDTO.fromCardId());
        Card target = cardRepository.getReferenceById(transactionDTO.toCardId());

        if (!Objects.equals(target.getUser().getId(), source.getUser().getId())) {
            throw new DifferentIdentifierException("Id of users of cards are different");
        }

        if (source.getStatus() != Status.ACTIVE || target.getStatus() != Status.ACTIVE) {
            throw new UnactiveCardException("Both cards must be active for transaction");
        }

        if (Objects.equals(target.getId(), source.getId())) {
            throw new SameCardException("The cards for transaction are the same");
        }

        if (transactionDTO.email().isEmpty()) {
            throw new NoEmailException("There is no email to send the transaction information");
        }

        //Вот, выбросим исключение если баланс негативный
        if (source.getBalance() - transactionDTO.amount() < 0) {
            throw new NegativeBalanceException("That amount of money is too huge for the card");
        }

        source.setBalance(source.getBalance() - transactionDTO.amount());
        target.setBalance(target.getBalance() + transactionDTO.amount());

        //todo: проверь без этого
        cardRepository.save(source);
        cardRepository.save(target);

        return transactionDTO;
    }

    @Transactional(readOnly = true)
    public List<CardDTO> findByUserId(Long userId, UserDetailsImpl userDetails) {

        if (!userDetails.getId().equals(userId)) {
            throw new DifferentIdentifierException("Идентификатор пользователя и владельца карты разные. В доступе отказано");
        }

        List<Card> cards = cardRepository.findAllByUserId(userId);

        return cards.stream()
                .map(cardMapper::makeACardDTO)
                .toList();
    }

    @Transactional
    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }
}
