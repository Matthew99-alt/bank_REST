package com.example.bankcards.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO для перевода денег между картами
*/

@Getter
@Setter
public class TransactionDTO {
    Long fromCardId;

    Long toCardId;

    Long amount;
}
