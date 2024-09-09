package org.jboss.set;

import org.jboss.set.model.json.BuildJMSTriggerPayload;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;

@ApplicationScoped
@Alternative
@Priority(1)
public class MockBuildTrigger extends BuildTrigger {

    @Override
    public void triggerBuild(BuildJMSTriggerPayload payloadMessage) {
        // test, do nothing
    }
}
