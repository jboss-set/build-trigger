package org.jboss.set;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import org.jboss.set.exception.PKBIndexNotLoadedException;
import org.jboss.set.model.json.ProjectInfo;
import org.jboss.set.pkb.PKBIndexer;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.Set;
import java.util.function.Function;

@Path("/")
public class FrontendResource {

    @Inject
    PKBIndexer pkbIndexer;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance index(Set<String> projectIds, String project);

        public static native TemplateInstance modify(Set<String> projectIds, String project);

        public static native TemplateInstance emptyIndex();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getIndex() {
        return getEmptyOr((projectIds) -> Templates.index(projectIds, null));
    }

    @GET
    @Path("{project}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getIndexWithProject(String project) {
        return getEmptyOr((projectIds) -> Templates.index(projectIds, project));
    }

    @GET
    @Path("modify")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getModify() {
        return getEmptyOr((projectIds) -> Templates.modify(projectIds, null));
    }

    @GET
    @Path("modify/{project}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getModifyWithProject(String project) {
        return getEmptyOr((projectIds) -> Templates.modify(projectIds, project));
    }

    @GET
    @Path("project/{id}")
    public ProjectInfo getProjectInfoFor(String id, @RestQuery String stream) {
        return ProjectInfo.from(pkbIndexer.getProject(id, stream));
    }

    @POST
    @Path("/pkb-reload")
    public void reloadPKB() throws PKBIndexNotLoadedException {
        pkbIndexer.loadProjects();
    }

    private TemplateInstance getEmptyOr(Function<Set<String>, TemplateInstance> templateFunction) {
        Set<String> projectIds = pkbIndexer.getProjectIds();
        return projectIds.isEmpty() ? Templates.emptyIndex() : templateFunction.apply(projectIds);
    }
}
