package org.jboss.set.model.json;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class BuildInfo {

    @NotNull
    public String project;

    @NotNull
    @Valid
    public Environment environment;

    @NotNull
    @Pattern(regexp = "^(https:\\/\\/\\S+|git\\+\\S+|git@\\S+)$", message = "Invalid format! The upstream must start with https://, git@ or git+")
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
