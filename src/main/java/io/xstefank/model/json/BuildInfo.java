package io.xstefank.model.json;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class BuildInfo {

    @NotNull
    public String project;

    @NotNull
    @Valid
    public Environment environment;

    @NotNull
    public String upstream;

    @NotNull
    public String revision;

    @NotNull
    public String script;

    @NotNull
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
