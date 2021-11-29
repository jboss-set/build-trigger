package io.xstefank.model.json;

import java.util.List;

public class BuildInfo {

    public String project;
    public List<String> product;
    public Environment environment;
    public String upstream;
    public String revision;
    public String script;
    public String version;

    @Override
    public String toString() {
        return "BuildInfo{" +
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
