package org.example.jakartaweb.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global provider intercepting Bean Validation failures triggered via @Valid annotations.
 * Transforms internal constraint exceptions into structured HTTP 400 Bad Request JSON payloads.
 */
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        // Collect all localized string messages from the violated constraint fields
        List<String> errors = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        ValidationErrorResponse responseBody = new ValidationErrorResponse(
                "Validation failed for the incoming request payload.",
                errors
        );

        // Terminate request lifecycle and return HTTP 400 with the array wrapper
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(responseBody)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}

