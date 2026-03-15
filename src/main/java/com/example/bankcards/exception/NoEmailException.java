package com.example.bankcards.exception;

public class NoEmailException extends RuntimeException {
    public NoEmailException(String message) {
        super(message);
    }
}
