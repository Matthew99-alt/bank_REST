package com.example.bankcards.service;

import com.example.bankcards.dto.CardUserDTO;
import com.example.bankcards.entity.CardUser;
import com.example.bankcards.exception.CustomEntityNotFoundException;
import com.example.bankcards.exception.UnuniqueParameterException;
import com.example.bankcards.mapper.CardUserMapper;
import com.example.bankcards.repository.CardUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс сервис отвечающий за реализацию запросов контроллера и работу с объектами класса Card
 * @see  CardUser
 * @see  CardUserRepository
 * @see  CardUserDTO
 * @see  CardUserMapper
 * @see  com.example.bankcards.controller.CardUserController
 */

@Service
@RequiredArgsConstructor
public class CardUserService {

    private final CardUserRepository cardUserRepository;
    private final CardUserMapper cardsUserMapper;

    @Transactional(readOnly = true)
    public List<CardUserDTO> findAllCardUsers() {
        return cardUserRepository.findAll()
                .stream()
                .map(cardsUserMapper::makeACardUserDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CardUserDTO findCardUserById(Long encodedId) {
        CardUser cardUser = cardUserRepository.findById(encodedId)
                .orElseThrow(() -> new CustomEntityNotFoundException("Указанный пользователь не найден"));
        return cardsUserMapper.makeACardUserDTO(cardUser);
    }

    @Transactional
    public CardUserDTO saveCardUser(CardUserDTO cardUserDTO){

        if (cardUserRepository.existsByEmail(cardUserDTO.getEmail())
                ||cardUserRepository.existsByPhoneNumber(cardUserDTO.getPhoneNumber())){
            throw new UnuniqueParameterException("Email and phone number should be unique");
        }

        CardUser savedCardUser = cardUserRepository.save(cardsUserMapper.makeACardUser(cardUserDTO));
        return cardsUserMapper.makeACardUserDTO(savedCardUser);
    }

    @Transactional
    public void deleteUserCard(Long id) {
        cardUserRepository.deleteById(id);
    }

    @Transactional
    public CardUserDTO editUserCard(CardUserDTO cardUserDTO) {
        CardUser cardUser = cardsUserMapper.makeACardUser(cardUserDTO);
        cardUserRepository.save(cardUser);
        return cardUserDTO;
    }
}
