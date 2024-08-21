package io.xstefank.model.json;

import io.xstefank.pnc.model.json.Artifact;

import java.util.ArrayList;
import java.util.List;

public class ProjectInfo {

    public String project;
    public EnvironmentTools environmentTools;
    public String upstream;
    public String revision;
    public String script;
    public String version;

    public static ProjectInfo from(Artifact project) {
        ProjectInfo buildInfo = new ProjectInfo();

        List<String> openjdk = new ArrayList<>();
        openjdk.add(project.environment.attributes.get("JDK"));
        List<String> maven = new ArrayList<>();
        maven.add(project.environment.attributes.get("MAVEN"));

        buildInfo.project = project.name;
        buildInfo.upstream = project.scmRepository.externalUrl;
        buildInfo.script = project.buildScript;
        buildInfo.environmentTools = new EnvironmentTools();
        buildInfo.environmentTools.openjdk = openjdk;
        buildInfo.environmentTools.maven = maven;

        return buildInfo;
    }

    @Override
    public String toString() {
        return "ProjectInfo{" +
            "project='" + project + '\'' +
            ", environmentTools=" + environmentTools +
            ", upstream='" + upstream + '\'' +
            ", revision='" + revision + '\'' +
            ", script='" + script + '\'' +
            ", version='" + version + '\'' +
            '}';
    }
}
