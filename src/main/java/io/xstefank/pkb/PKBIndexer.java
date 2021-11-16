package io.xstefank.pkb;

import io.quarkus.runtime.StartupEvent;
import io.xstefank.client.PKBClient;
import io.xstefank.pkb.model.json.Project;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class PKBIndexer {

    public Map<String, Project> projects;

    @Inject
    @RestClient
    PKBClient pkbClient;


    void onStart(@Observes StartupEvent event) {
        projects = new HashMap<>();

        pkbClient.getProjects().forEach(project -> {
            projects.put(project.project, project);
        });

        System.out.println(projects.keySet());
    }

    public Set<String> getProjectIds() {
        return Collections.unmodifiableSet(projects.keySet());
    }

    public Project getProject(String id) {
        Project project = projects.get(id);

        if (project == null) {
            throw new NotFoundException(String.format("Project '%s' not found", id));
        }

        return project;
    }
}
