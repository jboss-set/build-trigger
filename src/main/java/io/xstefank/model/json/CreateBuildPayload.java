package io.xstefank.model.json;

public class CreateBuildPayload {

    public String email;
    public Product product;
    public Environment environment;
    public String upstream;
    public String revision;
    public String script;
    public String version;

    public static CreateBuildPayload from(BuildInfo buildInfo, String email) {
        CreateBuildPayload createBuildPayload = new CreateBuildPayload();

        createBuildPayload.email = email;
        createBuildPayload.product = buildInfo.product;
        createBuildPayload.environment = buildInfo.environment;
        createBuildPayload.upstream = buildInfo.upstream;
        createBuildPayload.revision = buildInfo.revision;
        createBuildPayload.script = buildInfo.script;
        createBuildPayload.version = buildInfo.version;

        return createBuildPayload;
    }
}
