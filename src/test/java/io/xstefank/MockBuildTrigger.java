package io.xstefank;

import io.quarkus.arc.AlternativePriority;
import io.xstefank.model.json.BuildJMSModifyPayload;
import io.xstefank.model.json.BuildJMSTriggerPayload;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@AlternativePriority(1)
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
