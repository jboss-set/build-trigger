package io.xstefank.model.json;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
