package io.xstefank.pkb.model.json;

import java.util.List;

public class Project {

    public String project;
    public String owner;
    public String stream;
    public String ga;
    public BuildConfiguration buildConfiguration;
    public LastBuild lastBuild;
    public List<String> products;

    @Override
    public String toString() {
        return "Project{" +
            "project='" + project + '\'' +
            ", owner='" + owner + '\'' +
            ", stream='" + stream + '\'' +
            ", ga='" + ga + '\'' +
            ", buildConfiguration=" + buildConfiguration +
            ", lastBuild=" + lastBuild +
            ", products=" + products +
            '}';
    }
}
