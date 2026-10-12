package org.example.jakartaweb.validation;


import java.util.List;

/**
 * Data structure representing structural bean validation error outputs.
 * Provides a standardized format for multi-field validation constraint breaches.
 */
public class ValidationErrorResponse {
    private String message;
    private List<String> errors;

    public ValidationErrorResponse(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
    }

    public String getMessage() { return message; }
    public List<String> getErrors() { return errors; }
}

