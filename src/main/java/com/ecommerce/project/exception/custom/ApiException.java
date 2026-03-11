package com.ecommerce.project.exception.custom;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
