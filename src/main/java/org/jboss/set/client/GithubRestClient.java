package org.jboss.set.client;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/repos")
@RegisterRestClient(baseUri = "https://api.github.com/")
public interface GithubRestClient {

    @GET
    @Path("/{codeSpace}/{repository}/git/refs/tags/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    JsonNode getRefsInfo(@PathParam("codeSpace") String codeSpace,
                         @PathParam("repository") String repository,
                         @PathParam("version") String version,
                         @HeaderParam("Authorization") String bearerToken);

    @GET
    @Path("/{codeSpace}/{repository}/git/tags/{tagSHA}")
    @Produces(MediaType.APPLICATION_JSON)
    JsonNode getTagInfo(@PathParam("codeSpace") String codeSpace,
                        @PathParam("repository") String repository,
                        @PathParam("tagSHA") String tagSHA,
                        @HeaderParam("Authorization") String bearerToken);
}
