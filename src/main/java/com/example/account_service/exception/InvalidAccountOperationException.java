package com.example.account_service.exception;

public class InvalidAccountOperationException extends RuntimeException {
    public InvalidAccountOperationException(String message) {
        super(message);
    }
}