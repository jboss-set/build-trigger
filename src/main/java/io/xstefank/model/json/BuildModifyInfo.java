package io.xstefank.model.json;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class BuildModifyInfo {

    @NotNull
    public String project;

    @NotNull
    @Valid
    public EnvironmentTools environmentTools;

    @NotNull
    public String upstream;

    @NotNull
    public String script;

    @Override
    public String toString() {
        return "BuildModifyInfo{" +
            "project='" + project + '\'' +
            ", environmentTools=" + environmentTools +
            ", upstream='" + upstream + '\'' +
            ", script='" + script + '\'' +
            '}';
    }
}
