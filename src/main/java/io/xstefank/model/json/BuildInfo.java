package io.xstefank.model.json;

public class BuildInfo {

    public String project;
    public Environment environment;
    public String upstream;
    public String revision;
    public String script;
    public String version;

    @Override
    public String toString() {
        return "BuildInfo{" +
            "project='" + project + '\'' +
            ", environment=" + environment +
            ", upstream='" + upstream + '\'' +
            ", revision='" + revision + '\'' +
            ", script='" + script + '\'' +
            ", version='" + version + '\'' +
            '}';
    }
}
