package com.ecommerce.project.exception.custom;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, String fieldName, int fieldValue) {
        super(buildMessage(resource, fieldName, fieldValue));
    }

    private static String buildMessage(String resource, String fieldName, int fieldValue) {
        return String.format("%s not found with %s %s", resource, fieldName, fieldValue);
    }
}
