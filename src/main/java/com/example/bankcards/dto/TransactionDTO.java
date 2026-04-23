package com.example.bankcards.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

/**
 * DTO для перевода денег между картами
 */

public record TransactionDTO(
        @NotNull
        Long fromCardId,
        @NotNull
        Long toCardId,
        @NotNull
        @Positive(message = "Сумма перевода должна быть положительной")
        Long amount,
        @Email
        @NotBlank
        String email
) {
}
