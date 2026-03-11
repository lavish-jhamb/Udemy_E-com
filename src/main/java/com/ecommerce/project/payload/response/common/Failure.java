package com.ecommerce.project.payload.response.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL) // This annotation omit the null field, and we won't see any null field in response.
public record Failure(
        LocalDateTime timestamp,
        String path,
        int status,
        boolean success,
        String error,
        String code,
        String message,
        List<ErrorDetail> errors
) implements ApiResponse {

    // static factory method
    public static ResponseEntity<Failure> of(HttpStatus status, String path, String error, String code, String message, List<ErrorDetail> errors) {
        Failure body = new Failure(
                LocalDateTime.now(),
                path,
                status.value(),
                false,
                error,
                code,
                message,
                errors
        );
        return ResponseEntity.status(status).body(body);
    }

    public static ResponseEntity<Failure> of(HttpStatus status, String path, String error, String code, String message) {
        Failure body = new Failure(
                LocalDateTime.now(),
                path,
                status.value(),
                false,
                error,
                code,
                message,
                null
        );
        return ResponseEntity.status(status).body(body);
    }

}
