package io.xstefank.pkb;

import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.StartupEvent;
import io.xstefank.client.PKBClient;
import io.xstefank.pkb.model.json.Project;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class PKBIndexer {

    @Inject
    @RestClient
    PKBClient pkbClient;

    public Set<String> getProjectIds() {
        return getProjects().entrySet().stream()
            .flatMap(e -> e.getValue().stream()
                .map(v -> String.format("%s%s", e.getKey(), !v.stream.isBlank() ? " - " + v.stream : "")))
            .collect(Collectors.toUnmodifiableSet());
    }

    public Project getProject(String id, String stream) {
        List<Project> found = getProjects().get(id);

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

    private Map<String, List<Project>> getProjects() {
        Map<String, List<Project>> projects = new HashMap<>();

        if (!LaunchMode.current().equals(LaunchMode.TEST)) {
            pkbClient.getProjects().forEach(project -> {
                projects.computeIfAbsent(project.project, s -> new ArrayList<>());
                projects.get(project.project).add(project);
            });
        }

        return projects;
    }
}
