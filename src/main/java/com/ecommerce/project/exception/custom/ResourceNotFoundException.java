package com.ecommerce.project.exception.custom;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, String fieldName, int fieldValue) {
        super("%s not found with %s %s".formatted(resource, fieldName, fieldValue));
    }

    public ResourceNotFoundException(String resource, String fieldName, long fieldValue) {
        super("%s not found with %s %s".formatted(resource, fieldName, fieldValue));
    }

    public ResourceNotFoundException(String resource, String fieldName, String fieldValue) {
        super("%s not found with %s %s".formatted(resource, fieldName, fieldValue));
    }
}
