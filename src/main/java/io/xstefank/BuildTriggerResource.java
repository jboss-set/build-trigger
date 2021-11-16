package io.xstefank;

import io.quarkus.oidc.IdToken;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.xstefank.model.json.BuildInfo;
import io.xstefank.model.json.ProjectInfo;
import io.xstefank.model.json.TriggerBuildPayload;
import io.xstefank.pkb.PKBIndexer;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/")
public class BuildTriggerResource {

    Logger logger = Logger.getLogger(BuildTriggerResource.class);

    @Inject
    PKBIndexer pkbIndexer;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance index(Set<String> projectIds);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getIndex() {
        return Templates.index(pkbIndexer.getProjectIds());
    }

    @GET
    @Path("/{id}")
    public ProjectInfo getProjectInfoFor(@PathParam String id) {
        return ProjectInfo.from(pkbIndexer.getProject(id));
    }


    @Inject
    BuildTrigger buildTrigger;

    @Inject
    @IdToken
    JsonWebToken idToken;

    @POST
    @Path("/trigger")
    public Response triggerBuildFromUI(BuildInfo buildInfo) {
        String email = idToken.claim(Claims.email).orElse("Email not provided in the token").toString();
        logger.infof("Triggering build for product %s by %s", buildInfo, email);

        buildTrigger.triggerBuild(TriggerBuildPayload.from(buildInfo, email));

        return Response.ok("Triggered build for " + buildInfo.upstream + ":" + buildInfo.version).build();
    }
}
