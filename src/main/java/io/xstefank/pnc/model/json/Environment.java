package io.xstefank.pnc.model.json;

import java.util.Map;

public class Environment {

    public String id;
    public String name;
    public String description;
    public String systemImageRepositoryUrl;
    public String systemImageId;
    public Map<String, String> attributes;
    public String systemImageType;
    public boolean deprecated;
    public boolean hidden;
}
