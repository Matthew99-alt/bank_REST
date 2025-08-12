package com.example.bankcards.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * DTO для обработки информации о пользователе
*/

@Getter
@Setter
public class UserDTO {

    private Long id;

    private String firstName;

    private String secondName;

    private String middleName;

    private String email;

    private String password;

    private String phoneNumber;

    private Set<String> role;
}
