package io.xstefank.model.json;

public class BuildJMSTriggerPayload {

    public String email;
    public String project;
    public EnvironmentTools environmentTools;
    public String upstream;
    public String revision;
    public String script;
    public String version;

    public static BuildJMSTriggerPayload from(BuildInfo buildInfo, String email) {
        BuildJMSTriggerPayload createBuildPayload = new BuildJMSTriggerPayload();

        createBuildPayload.email = email;
        createBuildPayload.project = buildInfo.project;
        createBuildPayload.environmentTools = buildInfo.environmentTools;
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
            ", environmentTools=" + environmentTools +
            ", upstream='" + upstream + '\'' +
            ", revision='" + revision + '\'' +
            ", script='" + script + '\'' +
            ", version='" + version + '\'' +
            '}';
    }
}
