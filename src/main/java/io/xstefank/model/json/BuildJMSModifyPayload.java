package io.xstefank.model.json;

public class BuildJMSModifyPayload {

    public String email;
    public String project;
    public EnvironmentTools environmentTools;
    public String upstream;
    public String script;

    public static BuildJMSModifyPayload from(BuildModifyInfo buildModifyInfo, String email) {
        BuildJMSModifyPayload buildJMSModifyPayload = new BuildJMSModifyPayload();

        buildJMSModifyPayload.email = email;
        buildJMSModifyPayload.project = buildModifyInfo.project;
        buildJMSModifyPayload.environmentTools = buildModifyInfo.environmentTools;
        buildJMSModifyPayload.upstream = buildModifyInfo.upstream;
        buildJMSModifyPayload.script = buildModifyInfo.script;

        return buildJMSModifyPayload;
    }

    @Override
    public String toString() {
        return "BuildJMSModifyPayload{" +
            "email='" + email + '\'' +
            ", project='" + project + '\'' +
            ", environmentTools=" + environmentTools +
            ", upstream='" + upstream + '\'' +
            ", script='" + script + '\'' +
            '}';
    }
}
