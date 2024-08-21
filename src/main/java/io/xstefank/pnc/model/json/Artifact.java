package io.xstefank.pnc.model.json;

import java.util.Map;

public class Artifact {

    public String id;
    public String name;
    public String description;
    public String buildScript;
    public String scmRevision;
    public String creationTime;
    public String modificationTime;
    public String buildType;
    public String defaultAlignmentParams;
    public boolean brewPullActive;
    public ScmRepository scmRepository;
    public Project project;
    public Environment environment;
    public Object dependencies;
    public ProductVersion productVersion;
    public Object groupConfigs;
    public Map<String, String> parameters;
    public User creationUser;
    public User modificationUser;
}
