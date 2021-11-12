package io.xstefank;

import io.quarkus.oidc.IdToken;
import io.xstefank.model.json.BuildInfo;
import io.xstefank.model.json.CreateBuildPayload;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
public class BuildTriggerResource {

    Logger logger = Logger.getLogger(BuildTriggerResource.class);

    @Inject
    BuildTrigger buildTrigger;

    @Inject
    @IdToken
    JsonWebToken idToken;

    @POST
    @Path("/trigger")
    public Response triggerBuildFromUI(BuildInfo buildInfo) {
        String email = idToken.getClaim(Claims.email).toString();
        logger.infof("Triggering build for product %s by %s", buildInfo, email);

        buildTrigger.triggerBuild(CreateBuildPayload.from(buildInfo, email));

        return Response.ok("Triggered build for " + buildInfo.upstream + ":" + buildInfo.version).build();
    }
}
