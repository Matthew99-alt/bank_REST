package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardUserDTO;
import com.example.bankcards.entity.User;
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
    public User makeACardUser(CardUserDTO cardUserDTO) {
        User user = new User();

        user.setId(cardUserDTO.getId());
        user.setFirstName(cardUserDTO.getFirstName());
        user.setMiddleName(cardUserDTO.getMiddleName());
        user.setSecondName(cardUserDTO.getSecondName());
        user.setEmail(cardUserDTO.getEmail());
        user.setPassword(cardUserDTO.getPassword());
        user.setPhoneNumber(cardUserDTO.getPhoneNumber());

        return user;
    }
    public CardUserDTO makeACardUserDTO(User user) {
        CardUserDTO cardUserDTO = new CardUserDTO();

        cardUserDTO.setId(user.getId());
        cardUserDTO.setFirstName(user.getFirstName());
        cardUserDTO.setMiddleName(user.getMiddleName());
        cardUserDTO.setSecondName(user.getSecondName());
        cardUserDTO.setEmail(user.getEmail());
        cardUserDTO.setPhoneNumber(user.getPhoneNumber());

        Set<Long> roleIds = user.getRole().stream()
                .map(Role::getId)
                .collect(Collectors.toSet());
        cardUserDTO.setRoleIds(roleIds);

        return cardUserDTO;
    }
}
