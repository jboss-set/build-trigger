package io.xstefank;

import io.quarkus.oidc.IdToken;
import io.xstefank.model.json.BuildInfo;
import io.xstefank.model.json.BuildJMSModifyPayload;
import io.xstefank.model.json.BuildJMSTriggerPayload;
import io.xstefank.model.json.BuildModifyInfo;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

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
    public Response triggerBuild(BuildInfo buildInfo) {
        String email = getEmail();
        logger.infof("Triggering build for product %s by %s", buildInfo, email);

        buildTrigger.triggerBuild(BuildJMSTriggerPayload.from(buildInfo, email));

        return Response.ok("Triggered build for " + buildInfo.project + ":" + buildInfo.version).build();
    }

    @PUT
    @Path("/modify")
    public Response modifyBuild(BuildModifyInfo buildModifyInfo) {
        String email = getEmail();
        logger.infof("Triggering build modification for product %s by %s", buildModifyInfo, email);

        buildTrigger.modifyBuild(BuildJMSModifyPayload.from(buildModifyInfo, email));

        return Response.ok("Triggered build modification for " + buildModifyInfo.project).build();
    }

    private String getEmail() {
        return idToken.claim(Claims.email).orElse("Email not provided in the token").toString();
    }
}
