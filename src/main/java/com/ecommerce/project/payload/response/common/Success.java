package com.ecommerce.project.payload.response.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public record Success<T>(
        LocalDateTime timestamp,
        int status,
        boolean success,
        String message,
        T data
) implements ApiResponse {

    // static factory method
    public static <T> ResponseEntity<Success<T>> of(HttpStatus status, String message, T data) {
        Success<T> body = new Success<>(
                LocalDateTime.now(),
                status.value(),
                true,
                message,
                data
        );
        return ResponseEntity.status(status).body(body);
    }

}
