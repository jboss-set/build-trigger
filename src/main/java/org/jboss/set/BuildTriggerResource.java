package org.jboss.set;

import io.quarkus.oidc.IdToken;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.QueryParam;
import org.jboss.set.model.json.BuildInfo;
import org.jboss.set.model.json.BuildJMSTriggerPayload;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import java.net.MalformedURLException;
import java.net.URL;

@Path("/build-trigger")
public class BuildTriggerResource {

    Logger logger = Logger.getLogger(BuildTriggerResource.class);

    @Inject
    BuildTrigger buildTrigger;

    @Inject
    GitBuildInfoAssembler gitBuildInfoAssembler;

    @Inject
    @IdToken
    JsonWebToken idToken;

    @POST
    @Path("/trigger")
    public Response triggerBuild(@NotNull @Valid BuildInfo buildInfo) {
        String email = idToken.claim(Claims.email).orElse("Email not provided in the token").toString();
        if (buildInfo.streams == null || buildInfo.streams.isEmpty()) {
            return Response.status(400, "List of streams is empty").build();
        }
        logger.infof("Triggering build for product %s by %s", buildInfo, email);

        buildTrigger.triggerBuild(BuildJMSTriggerPayload.from(buildInfo, email));

        return Response.ok("Triggered build for " + buildInfo.gitRepo + ":" + buildInfo.projectVersion).build();
    }

    @GET
    @Path("/getBuildInfo")
    public Response getBuildInfoFromGit(@NotNull @Valid @QueryParam("tag") String url) {
        logger.info("Fetching remote repository: " + url);
        try {
            BuildInfo buildInfo = gitBuildInfoAssembler.constructBuildFromURL(new URL(url));
            if (buildInfo == null) {
                return Response.noContent().build();
            }
            return Response.ok(buildInfo).build();
        } catch (MalformedURLException e) {
            logger.error("Invalid URL. Cannot obtain tag data.");
            return null;
        }
    }
}
