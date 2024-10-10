package org.jboss.set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Logger;
import org.jboss.set.model.json.BuildJMSTriggerPayload;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Session;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class BuildTrigger {

    Logger logger = Logger.getLogger(BuildTrigger.class);

    @ConfigProperty(name = "build-trigger.topic")
    String triggerTopic;

    @Inject
    ConnectionFactory connectionFactory;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void triggerBuild(BuildJMSTriggerPayload payloadMessage) {
        try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
            context.createProducer().send(context.createTopic(triggerTopic), objectMapper.writeValueAsString(payloadMessage));
        } catch (JsonProcessingException e) {
            logger.errorf("Failed to serialize BuildJMSTriggerPayload to JSON: "
                    + e.getMessage() + ". Payload: " + payloadMessage);
            throw new RuntimeException("Failed to serialize BuildJMSTriggerPayload to JSON: " + e.getMessage(), e);
        }
    }
}
