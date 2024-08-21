package io.xstefank.pnc;

import io.quarkus.logging.Log;
import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.StartupEvent;
import io.xstefank.client.PNCClient;
import io.xstefank.exception.PNCIndexNotLoadedException;
import io.xstefank.pnc.model.json.Artifact;
import io.xstefank.pnc.model.json.EAP80BuildConfigs;
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

@ApplicationScoped
public class PNCIndexer {

    public static int MAX_PAGE_SIZE = 200;

    public Map<String, List<Artifact>> projects;

    @Inject
    @RestClient
    PNCClient pncClient;

    void onStart(@Observes StartupEvent event) {
        try {
            loadProjects();
        } catch (PNCIndexNotLoadedException e) {
            projects = new HashMap<>();
        }
    }

    public Set<String> getProjectIds() {
        return projects.keySet();
    }

    public Artifact getProject(String projectName) {
        List<Artifact> found = projects.get(projectName);

        if (found == null) {
            throw new NotFoundException(String.format("Project '%s' not found", projectName));
        } else {
            return found.get(0);
        }
    }

    public void loadProjects() throws PNCIndexNotLoadedException {
        projects = new HashMap<>();

        if (!LaunchMode.current().equals(LaunchMode.TEST)) {
            try {
                EAP80BuildConfigs eap80BuildConfigs = pncClient.getEAP80Projects(0, MAX_PAGE_SIZE);
                eap80BuildConfigs.content.forEach(project -> {
                    projects.computeIfAbsent(project.name, s -> new ArrayList<>());
                    projects.get(project.name).add(project);
                });

                int i = MAX_PAGE_SIZE;
                int pageIndex = 1;
                //The maximum page size of PNC is 200. If the number of projects exceeds 200, we need to make
                //another call to obtain projects associated with the given product.
                while (i < eap80BuildConfigs.totalHits) {
                    Map<String, List<Artifact>> restOfProjects = new HashMap<>();

                    //Here, we are trying to get the second page of the projects if there are more than can be displayed on one page.
                    pncClient.getEAP80Projects(pageIndex, MAX_PAGE_SIZE).content.forEach(project -> {
                        restOfProjects.computeIfAbsent(project.name, s -> new ArrayList<>());
                        restOfProjects.get(project.name).add(project);
                    });

                    projects.putAll(restOfProjects);
                    i += MAX_PAGE_SIZE;
                    pageIndex++;
                }


//
//
//                if (eap80BuildConfigs.totalHits > (2 * MAX_PAGE_SIZE)) {
//                    //The maximum page size of PNC is 200. This situation should not happen, but if the number of total
//                    //projects exceeds 400, we need to make another call to obtain all projects associated with the given product.
//                    throw new PNCIndexNotLoadedException("Not all projects have been loaded!");
//                }
//
//                eap80BuildConfigs.content.forEach(project -> {
//                    projects.computeIfAbsent(project.name, s -> new ArrayList<>());
//                    projects.get(project.name).add(project);
//                });
//
//                if (eap80BuildConfigs.totalHits > MAX_PAGE_SIZE) {
//                    Map<String, List<Artifact>> restOfProjects = new HashMap<>();
//
//                    //Here, we are trying to get the second page of the projects if there are more than can be displayed on one page.
//                    pncClient.getEAP80Projects(1, MAX_PAGE_SIZE).content.forEach(project -> {
//                        restOfProjects.computeIfAbsent(project.name, s -> new ArrayList<>());
//                        restOfProjects.get(project.name).add(project);
//                    });
//
//                    projects.putAll(restOfProjects);
//                }
            } catch (Exception e) {
                Log.error("Cannot load PNC index", e);
                throw new PNCIndexNotLoadedException(e);
            }
        }
    }
}
