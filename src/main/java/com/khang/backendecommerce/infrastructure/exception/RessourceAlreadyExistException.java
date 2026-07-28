package com.khang.backendecommerce.infrastructure.exception;

public class RessourceAlreadyExistException extends RuntimeException {
    public RessourceAlreadyExistException(String message) {
        super(message);
    }
}
