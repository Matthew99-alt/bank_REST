package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardUserDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardUserMapper {
    public CardUser makeACardUser(CardUserDTO cardUserDTO) {
        CardUser cardUser = new CardUser();

        cardUser.setId(cardUserDTO.getId());
        cardUser.setFirstName(cardUserDTO.getFirstName());
        cardUser.setMiddleName(cardUserDTO.getMiddleName());
        cardUser.setSecondName(cardUserDTO.getSecondName());
        cardUser.setEmail(cardUserDTO.getEmail());
        cardUser.setPhoneNumber(cardUserDTO.getPhoneNumber());

        return cardUser;
    }
    public CardUserDTO makeACardUserDTO(CardUser cardUser) {
        CardUserDTO cardUserDTO = new CardUserDTO();

        cardUserDTO.setId(cardUser.getId());
        cardUserDTO.setFirstName(cardUser.getFirstName());
        cardUserDTO.setMiddleName(cardUser.getMiddleName());
        cardUserDTO.setSecondName(cardUser.getSecondName());
        cardUserDTO.setEmail(cardUser.getEmail());
        cardUserDTO.setPhoneNumber(cardUser.getPhoneNumber());

        return cardUserDTO;
    }
}
