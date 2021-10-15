package io.xstefank.model.json;

import java.util.List;

public class Environment {

    public List<String> openjdk;
    public List<String> maven;

    @Override
    public String toString() {
        return "Environment{" +
            "openjdk=" + openjdk +
            ", maven=" + maven +
            '}';
    }
}
