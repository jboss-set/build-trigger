package io.xstefank.model.json;

import io.xstefank.pkb.model.json.Project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectInfo {

    public String project;
    public List<String> product;
    public Environment environment;
    public String upstream;
    public String revision;
    public String script;
    public String version;

    public static ProjectInfo from(Project project) {
        ProjectInfo buildInfo = new ProjectInfo();

        buildInfo.project = project.project;
        buildInfo.upstream = project.buildConfiguration.source.scmUrl;
        buildInfo.script = project.buildConfiguration.script;
        buildInfo.product = project.products;
        buildInfo.environment = new Environment();
        buildInfo.environment.openjdk = parseList(project.buildConfiguration.environmentSpec.tools.openjdk);
        buildInfo.environment.maven = parseList(project.buildConfiguration.environmentSpec.tools.maven);

        return buildInfo;
    }

    private static List<String> parseList(String input) {
        List<String> list = new ArrayList<>();

        if (input == null) {
            return list;
        }

        Arrays.stream(input.split(",")).forEach(s -> list.add(s.trim()));

        return list;
    }

    @Override
    public String toString() {
        return "ProjectInfo{" +
            "project='" + project + '\'' +
            ", product=" + product +
            ", environment=" + environment +
            ", upstream='" + upstream + '\'' +
            ", revision='" + revision + '\'' +
            ", script='" + script + '\'' +
            ", version='" + version + '\'' +
            '}';
    }
}
