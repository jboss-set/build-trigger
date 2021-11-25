package io.xstefank.model.json;

import java.util.List;

public class BuildModifyInfo {

    public String project;
    public List<String> product;
    public Environment environment;
    public String upstream;
    public String script;

    @Override
    public String toString() {
        return "BuildModifyInfo{" +
            "project='" + project + '\'' +
            ", product=" + product +
            ", environment=" + environment +
            ", upstream='" + upstream + '\'' +
            ", script='" + script + '\'' +
            '}';
    }
}
