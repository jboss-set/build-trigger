package io.xstefank.pkb;

import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.StartupEvent;
import io.xstefank.client.PKBClient;
import io.xstefank.pkb.model.json.Project;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class PKBIndexer {

    public Map<String, List<Project>> projects;

    @Inject
    @RestClient
    PKBClient pkbClient;

    void onStart(@Observes StartupEvent event) {
        loadProjects();
    }

    public Set<String> getProjectIds() {
        return projects.entrySet().stream()
            .flatMap(e -> e.getValue().stream()
                .map(v -> String.format("%s%s", e.getKey(), !v.stream.isBlank() ? " - " + v.stream : "")))
            .collect(Collectors.toUnmodifiableSet());
    }

    public Project getProject(String id, String stream) {
        List<Project> found = projects.get(id);

        if (found == null) {
            throw new NotFoundException(String.format("Project '%s' not found", id));
        }

        if (stream != null) {
            Project project = found.stream().filter(p -> p.stream.equals(stream)).findFirst()
                .orElseThrow(() ->
                    new NotFoundException(String.format("Project '%s' for stream '%s' not found", id, stream)));
            return project;
        }

        return found.stream().filter(p -> p.stream.equals("")).findFirst()
            .orElseThrow(() -> new NotFoundException(String.format("Project '%s' not found", id)));
    }

    private void loadProjects() {
        projects = new HashMap<>();

        if (!LaunchMode.current().equals(LaunchMode.TEST)) {
            pkbClient.getProjects().forEach(project -> {
                projects.computeIfAbsent(project.project, s -> new ArrayList<>());
                projects.get(project.project).add(project);
            });
        }
    }
}
