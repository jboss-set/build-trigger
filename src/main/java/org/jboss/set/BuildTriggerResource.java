package org.jboss.set;

import io.quarkus.oidc.IdToken;
import org.jboss.set.model.json.BuildInfo;
import org.jboss.set.model.json.BuildJMSModifyPayload;
import org.jboss.set.model.json.BuildJMSTriggerPayload;
import org.jboss.set.model.json.BuildModifyInfo;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

@Path("/build-trigger")
public class BuildTriggerResource {

    Logger logger = Logger.getLogger(BuildTriggerResource.class);

    @Inject
    BuildTrigger buildTrigger;

    @Inject
    @IdToken
    JsonWebToken idToken;

    @POST
    @Path("/trigger")
    public Response triggerBuild(@NotNull @Valid BuildInfo buildInfo) {
        String email = getEmail();
        logger.infof("Triggering build for product %s by %s", buildInfo, email);

        buildTrigger.triggerBuild(BuildJMSTriggerPayload.from(buildInfo, email));

        return Response.ok("Triggered build for " + buildInfo.project + ":" + buildInfo.version).build();
    }

    @PUT
    @Path("/modify")
    public Response modifyBuild(@NotNull @Valid BuildModifyInfo buildModifyInfo) {
        String email = getEmail();
        logger.infof("Triggering build modification for product %s by %s", buildModifyInfo, email);

        buildTrigger.modifyBuild(BuildJMSModifyPayload.from(buildModifyInfo, email));

        return Response.ok("Triggered build modification for " + buildModifyInfo.project).build();
    }

    private String getEmail() {
        return idToken.claim(Claims.email).orElse("Email not provided in the token").toString();
    }
}
