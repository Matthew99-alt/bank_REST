package com.example.bankcards.exception;


/**
 * Кастомное иксключение для случаев не найденного пользователя ли карты
 */

public class CustomEntityNotFoundException extends RuntimeException {
    public CustomEntityNotFoundException(String message) {
        super(message);
    }
}
