package com.example.account_service.exception;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
class ErrorResponse {
    private String errorCode;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> details;

    public ErrorResponse(String errorCode, String message, LocalDateTime timestamp) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ErrorResponse(String errorCode, String message, LocalDateTime timestamp, Map<String, String> details) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = timestamp;
        this.details = details;
    }
}