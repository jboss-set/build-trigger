package io.xstefank.model.json;

import jakarta.validation.constraints.Size;
import java.util.List;

public class Environment {

    @Size(min = 1)
    public List<String> openjdk;

    @Size(min = 1)
    public List<String> maven;

    @Override
    public String toString() {
        return "Environment{" +
            "openjdk=" + openjdk +
            ", maven=" + maven +
            '}';
    }
}
