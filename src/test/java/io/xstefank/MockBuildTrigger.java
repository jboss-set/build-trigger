package io.xstefank;

import io.xstefank.model.json.BuildJMSModifyPayload;
import io.xstefank.model.json.BuildJMSTriggerPayload;
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

    @Override
    public void modifyBuild(BuildJMSModifyPayload payloadMessage) {
        // test, do nothing
    }
}
