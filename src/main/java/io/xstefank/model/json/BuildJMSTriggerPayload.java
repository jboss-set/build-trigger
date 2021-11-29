package io.xstefank.model.json;

import java.util.List;

public class BuildJMSTriggerPayload {

    public String email;
    public String project;
    public List<String> product;
    public Environment environment;
    public String upstream;
    public String revision;
    public String script;
    public String version;

    public static BuildJMSTriggerPayload from(BuildInfo buildInfo, String email) {
        BuildJMSTriggerPayload createBuildPayload = new BuildJMSTriggerPayload();

        createBuildPayload.email = email;
        createBuildPayload.project = buildInfo.project;
        createBuildPayload.product = buildInfo.product;
        createBuildPayload.environment = buildInfo.environment;
        createBuildPayload.upstream = buildInfo.upstream;
        createBuildPayload.revision = buildInfo.revision;
        createBuildPayload.script = buildInfo.script;
        createBuildPayload.version = buildInfo.version;

        return createBuildPayload;
    }

    @Override
    public String toString() {
        return "BuildJMSTriggerPayload{" +
            "email='" + email + '\'' +
            ", project='" + project + '\'' +
            ", product=" + product +
            ", environment=" + environment +
            ", upstream='" + upstream + '\'' +
            ", revision='" + revision + '\'' +
            ", script='" + script + '\'' +
            ", version='" + version + '\'' +
            '}';
    }
}
