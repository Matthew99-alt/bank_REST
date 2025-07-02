package com.example.bankcards.entity;

import com.example.bankcards.util.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Этот класс представляет данные таблицы card_user в виде объекта класса CardUser
 * Класс предоставляет данные о пользователях карт
 * Снабжён геттерами и сеттерами через Lombock
 * Является сущностью
 */

@Getter
@Setter
@Entity
@Table(name = "card_user")
public class CardUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "second_name", nullable = false)
    private String secondName;

    @Column(name = "middle_name", nullable = false)
    private String middleName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "card_user_role", nullable = false)
    private Role role;


}
