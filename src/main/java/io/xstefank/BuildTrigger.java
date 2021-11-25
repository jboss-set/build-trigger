package io.xstefank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.xstefank.client.PKBClient;
import io.xstefank.model.json.BuildJMSModifyPayload;
import io.xstefank.model.json.BuildJMSTriggerPayload;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Session;

@ApplicationScoped
public class BuildTrigger {

    @ConfigProperty(name = "build-trigger.topic")
    String triggerTopic;

    @ConfigProperty(name = "build-modify.topic")
    String modifyTopic;

    @Inject
    ConnectionFactory connectionFactory;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    @RestClient
    PKBClient pkbClient;

    public void triggerBuild(BuildJMSTriggerPayload payloadMessage) {
        sendJMS(triggerTopic, payloadMessage);
    }

    public void modifyBuild(BuildJMSModifyPayload payloadMessage) {
        sendJMS(modifyTopic, payloadMessage);
    }

    private void sendJMS(String topic, Object payload) {
        System.out.println(topic + payload);
//        try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
//            context.createProducer().send(context.createTopic(topic), objectMapper.writeValueAsString(payload));
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
    }
}
