package com.example.bankcards.service;

import com.example.bankcards.dto.CardUserDTO;
import com.example.bankcards.entity.CardUser;
import com.example.bankcards.exception.CustomEntityNotFoundException;
import com.example.bankcards.exception.UnuniqueParameterException;
import com.example.bankcards.mapper.CardUserMapper;
import com.example.bankcards.repository.CardUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardUserService {

    private final CardUserRepository cardUserRepository;
    private final CardUserMapper cardsUserMapper;

    public List<CardUserDTO> findAllCardUsers() {
        return cardUserRepository.findAll()
                .stream()
                .map(cardsUserMapper::makeACardUserDTO)
                .toList();
    }

    public CardUserDTO findCardUserById(Long encodedId) {
        CardUser cardUser = cardUserRepository.findById(encodedId)
                .orElseThrow(() -> new CustomEntityNotFoundException("Указанный пользователь не найден"));
        return cardsUserMapper.makeACardUserDTO(cardUser);
    }

    public CardUserDTO saveCardUser(CardUserDTO cardUserDTO){

        if (cardUserRepository.existsByEmail(cardUserDTO.getEmail())
                ||cardUserRepository.existsByPhoneNumber(cardUserDTO.getPhoneNumber())){
            throw new UnuniqueParameterException("Email and phone number should be unique");
        }

        CardUser savedCardUser = cardUserRepository.save(cardsUserMapper.makeACardUser(cardUserDTO));
        return cardsUserMapper.makeACardUserDTO(savedCardUser);
    }

    public void deleteUserCard(Long id) {
        cardUserRepository.deleteById(id);
    }

    public CardUserDTO editUserCard(CardUserDTO cardUserDTO) {
        CardUser cardUser = cardsUserMapper.makeACardUser(cardUserDTO);
        cardUserRepository.save(cardUser);
        return cardUserDTO;
    }
}
