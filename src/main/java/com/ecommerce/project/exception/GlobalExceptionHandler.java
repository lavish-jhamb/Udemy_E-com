package com.ecommerce.project.exception;

import com.ecommerce.project.exception.custom.ApiException;
import com.ecommerce.project.exception.custom.ResourceNotFoundException;
import com.ecommerce.project.payload.response.common.ErrorDetail;
import com.ecommerce.project.payload.response.common.Failure;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Failure> handleHibernateValidationError(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Validation error occurred at URI: {}", request.getRequestURI(), ex);

        List<ErrorDetail> fieldErrors = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(error -> new ErrorDetail(error.getField(), error.getDefaultMessage()))
                .toList();

        return Failure.of(
                HttpStatus.BAD_REQUEST,
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ErrorCode.VALIDATION_ERROR.name(),
                "Invalid input provided",
                fieldErrors
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Failure> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("Resource not found at URI: {} - {}", request.getRequestURI(), ex.getMessage(), ex);

        return Failure.of(
                HttpStatus.NOT_FOUND,
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ErrorCode.RESOURCE_NOT_FOUND.name(),
                ex.getMessage()
        );
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Failure> handleApiException(ApiException ex, HttpServletRequest request) {
        log.warn("API exception occurred at URI: {} - {}", request.getRequestURI(), ex.getMessage(), ex);

        return Failure.of(
                HttpStatus.BAD_REQUEST,
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ErrorCode.BAD_REQUEST.name(),
                ex.getMessage()
        );
    }

    // Handle Generic Exception (Fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Failure> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected exception occurred at URI: {}", request.getRequestURI(), ex);

        return Failure.of(
                HttpStatus.INTERNAL_SERVER_ERROR,
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ErrorCode.INTERNAL_ERROR.name(),
                ex.getMessage()
        );
    }

}
