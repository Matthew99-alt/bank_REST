package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardUserDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardUser;
import com.example.bankcards.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Маппер, отдельный класс для выполнения операций по переводу сущности пользователя в DTO и наоборот
*/

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
        cardUser.setPassword(cardUserDTO.getPassword());
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

        Set<Long> roleIds = cardUser.getRole().stream()
                .map(Role::getId)
                .collect(Collectors.toSet());
        cardUserDTO.setRoleIds(roleIds);

        return cardUserDTO;
    }
}
