package com.example.bankcards.dto;

import com.example.bankcards.entity.Card;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * DTO для обработки информации о пользователе
*/

@Getter
@Setter
public class CardUserDTO {

    private Long id;

    private String firstName;

    private String secondName;

    private String middleName;

    private String email;

    private String password;

    private String phoneNumber;

    private Set<Long> roleIds;
}
