package org.example.jakartaweb.validation;

/**
 * Standard generalized error wrapper for singular API faults.
 * Used to deliver consistent JSON objects for specific non-validation infrastructure errors.
 */
public class ErrorResponse {
    private String message;
    private int status;

    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() { return message; }
    public int getStatus() { return status; }
}

