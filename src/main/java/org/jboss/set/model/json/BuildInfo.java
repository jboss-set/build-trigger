package org.jboss.set.model.json;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class BuildInfo {

    @NotNull
    public String tag;

    @NotNull
    public String gitRepo;

    @NotNull
    public String projectVersion;

    @NotNull
    public String commitSha;

    public List<String> streams;

    @Override
    public String toString() {
        return "BuildInfo{" +
            "tag='" + tag + '\'' +
            ", gitRepo=" + gitRepo +
            ", projectVersion='" + projectVersion + '\'' +
            ", commitSha='" + commitSha + '\'' +
            ", streams='" + streams + '\'' +
            '}';
    }
}
