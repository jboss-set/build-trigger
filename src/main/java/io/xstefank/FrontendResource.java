package io.xstefank;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.xstefank.model.json.ProjectInfo;
import io.xstefank.pkb.PKBIndexer;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Set;

@Path("/")
public class FrontendResource {

    @Inject
    PKBIndexer pkbIndexer;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance index(Set<String> projectIds);

        public static native TemplateInstance modify(Set<String> projectIds);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getIndex() {
        return Templates.index(pkbIndexer.getProjectIds());
    }

    @GET
    @Path("/modify")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getModify() {
        return Templates.modify(pkbIndexer.getProjectIds());
    }

    @GET
    @Path("/project/{id}")
    public ProjectInfo getProjectInfoFor(@PathParam String id, @QueryParam String stream) {
        return ProjectInfo.from(pkbIndexer.getProject(id, stream));
    }
}
