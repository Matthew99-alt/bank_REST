package com.example.bankcards.exception;

public class SameCardException extends RuntimeException {
    public SameCardException(String message) {
        super(message);
    }
}
