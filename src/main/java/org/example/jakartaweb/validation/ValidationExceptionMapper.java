package org.example.jakartaweb.validation;

import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Intercepts generic validation exceptions thrown across the application layers.
 * Normalizes global validation system constraints into an elegant HTTP 400 response.
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException exception) {
        ErrorResponse error = new ErrorResponse(
                "Invalid request payload configuration: " + exception.getMessage(),
                400
        );

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}

