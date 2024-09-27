package org.jboss.set.client;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "https://gitlab.com/")
public interface GitlabRestClient {

    @GET
    @Path("/api/v4/projects/{repoEncoded}/repository/tags/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    JsonNode getTagInfo(@PathParam("repoEncoded") String repoEncoded,
                        @PathParam("version") String version);
}
