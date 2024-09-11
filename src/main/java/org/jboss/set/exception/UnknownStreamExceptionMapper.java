package org.jboss.set.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnknownStreamExceptionMapper implements ExceptionMapper<UnknownStreamException> {
    @Override
    public Response toResponse(UnknownStreamException exception) {
        return Response.status(422)
                .entity(exception.getMessage())
                .build();
    }
}
