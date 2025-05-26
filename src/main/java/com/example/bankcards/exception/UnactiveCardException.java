package com.example.bankcards.exception;

public class UnactiveCardException extends RuntimeException {
    public UnactiveCardException(String message) {
        super(message);
    }
}
