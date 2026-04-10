package com.example.bankcards.dto;

import lombok.Getter;

/**
 * DTO для перевода денег между картами
 */

//TODO: валидации для ДТО
public record TransactionDTO(
        Long fromCardId,
        Long toCardId,
        Long amount,
        String email
) {
}
