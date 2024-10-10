package org.jboss.set;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import org.jboss.set.model.json.Stream;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/")
public class FrontendResource {

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance index(List<Stream> streams);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getIndex() {
        return Templates.index(List.of(Stream.values()));
    }

}
