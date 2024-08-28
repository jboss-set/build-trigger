package org.jboss.set.pkb.model.json;

import java.util.Map;

public class BuildConfiguration {

    public String id;
    public String environment;
    public EnvironmentSpec environmentSpec;
    public String imageId;
    public String versionPattern;
    public String script;
    public String testScriptVars;
    public Source source;
    public String system;
    public Map<String, String> parameters;
}
