package com.example.bankcards.exception;

public class DifferentIdentifierException extends RuntimeException {
    public DifferentIdentifierException(String message) {
        super(message);
    }
}
