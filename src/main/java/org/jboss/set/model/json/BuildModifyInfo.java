package org.jboss.set.model.json;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class BuildModifyInfo {

    @NotNull
    public String project;

    @NotNull
    @Valid
    public Environment environment;

    @NotNull
    public String upstream;

    @NotNull
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
