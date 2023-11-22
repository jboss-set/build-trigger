package io.xstefank.client;

import io.xstefank.pkb.model.json.Project;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.util.List;

@Path("/api/v1")
@RegisterRestClient(configKey = "pkb-client")
public interface PKBClient {

    @GET
    @Path("/project-stream/")
    List<Project> getProjects();
}
