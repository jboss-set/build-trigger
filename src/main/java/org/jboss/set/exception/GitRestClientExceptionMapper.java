package org.jboss.set.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class GitRestClientExceptionMapper implements ExceptionMapper<GitRestClientException> {

    Logger logger = Logger.getLogger(GitRestClientExceptionMapper.class);

    @Override
    public Response toResponse(GitRestClientException exception) {
        int responseCode = exception.getResponse().getStatus();

        logger.error(String.format("Invocation returned HTTP %s %s", responseCode, exception.getResponse().getStatusInfo().getReasonPhrase()));

        switch (responseCode) {
            case 404 -> {
                logger.warn("Requested tag not found");
                return Response.status(404)
                        .entity(exception.getMessage())
                        .build();
            }
            case 403 -> {
                logger.warn("Access denied. This is likely caused by expired token, please contact Build Trigger owners");
                return Response.status(403)
                        .entity(exception.getMessage())
                        .build();
            }
            default -> {
                return Response.status(500)
                        .entity(exception.getMessage())
                        .build();
            }
        }
    }
}
