package io.xstefank;

import io.xstefank.model.json.BuildInfo;
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

    @POST
    @Path("/trigger")
    public Response triggerBuildFromUI(BuildInfo buildInfo) {
        logger.info("Triggering build for: " + buildInfo);

        buildTrigger.triggerBuild(buildInfo);

        return Response.ok("Triggered build for " + buildInfo.upstream + ":" + buildInfo.version).build();
    }
}
