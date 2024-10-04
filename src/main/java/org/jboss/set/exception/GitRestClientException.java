package org.jboss.set.exception;

import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

public class GitRestClientException extends ClientWebApplicationException {
    public GitRestClientException(String message, Throwable cause, Response response) {
        super(message, cause, response);
    }
}