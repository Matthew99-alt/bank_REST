package com.example.bankcards.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {
    Long fromCardId;

    Long toCardId;

    Long amount;
}
