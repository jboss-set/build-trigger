package io.xstefank;

import io.quarkus.arc.AlternativePriority;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.enterprise.context.ApplicationScoped;
import java.util.Set;

@ApplicationScoped
@AlternativePriority(1)
public class MockJsonWebToken implements JsonWebToken {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public Set<String> getClaimNames() {
        return null;
    }

    @Override
    public <T> T getClaim(String s) {
        return null;
    }
}
