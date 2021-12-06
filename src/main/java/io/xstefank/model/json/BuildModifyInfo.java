package io.xstefank.model.json;

public class BuildModifyInfo {

    public String project;
    public Environment environment;
    public String upstream;
    public String script;

    @Override
    public String toString() {
        return "BuildModifyInfo{" +
            "project='" + project + '\'' +
            ", environment=" + environment +
            ", upstream='" + upstream + '\'' +
            ", script='" + script + '\'' +
            '}';
    }
}
