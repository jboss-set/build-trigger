package io.xstefank.model.json;

import jakarta.validation.constraints.Size;
import java.util.List;

public class EnvironmentTools {

    @Size(min = 1)
    public List<String> openjdk;

    @Size(min = 1)
    public List<String> maven;

    @Override
    public String toString() {
        return "EnvironmentTools{" +
            "openjdk=" + openjdk +
            ", maven=" + maven +
            '}';
    }
}
