package io.xstefank;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.xstefank.model.json.ProjectInfo;
import io.xstefank.pkb.PKBIndexer;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.Set;

@Path("/")
public class FrontendResource {

    @Inject
    PKBIndexer pkbIndexer;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance index(Set<String> projectIds, String project);

        public static native TemplateInstance modify(Set<String> projectIds, String project);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getIndex() {
        return Templates.index(pkbIndexer.getProjectIds(), null);
    }

    @GET
    @Path("{project}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getIndexWithProject(@PathParam String project) {
        return Templates.index(pkbIndexer.getProjectIds(), project);
    }

    @GET
    @Path("modify")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getModify() {
        return Templates.modify(pkbIndexer.getProjectIds(), null);
    }

    @GET
    @Path("modify/{project}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getModifyWithProjecyt(@PathParam String project) {
        return Templates.modify(pkbIndexer.getProjectIds(), project);
    }

    @GET
    @Path("project/{id}")
    public ProjectInfo getProjectInfoFor(@PathParam String id, @QueryParam String stream) {
        return ProjectInfo.from(pkbIndexer.getProject(id, stream));
    }
}
