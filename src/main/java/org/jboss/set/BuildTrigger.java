package org.jboss.set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSRuntimeException;
import org.jboss.set.exception.BuildTriggerException;
import org.jboss.set.model.json.BuildJMSTriggerPayload;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Session;
import org.jboss.logging.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class BuildTrigger {

    Logger logger = Logger.getLogger(BuildTrigger.class);

    @Inject
    EmailService emailService;

    @ConfigProperty(name = "build-trigger.topic")
    String triggerTopic;

    @Inject
    ConnectionFactory connectionFactory;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void triggerBuild(BuildJMSTriggerPayload payloadMessage) {
        try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
            context.createProducer().send(context.createTopic(triggerTopic), objectMapper.writeValueAsString(payloadMessage));
            logger.infof("Build message successfully sent to UMB for repository: %s, version: %s",
                    payloadMessage.gitRepo, payloadMessage.projectVersion);
                    if (payloadMessage.email != null && !payloadMessage.email.isBlank()
                            && !payloadMessage.email.equals("Email not provided in the token")) {
                        emailService.sendMail(payloadMessage);
                    } else {
                        logger.warnf("Confirmation email not sent: missing or invalid email");
                    }
        } catch (JsonProcessingException e) {
            logger.errorf("Failed to serialize BuildJMSTriggerPayload to JSON: "
                    + e.getMessage() + ". Payload: " + payloadMessage);
            throw new BuildTriggerException("Failed to serialize BuildJMSTriggerPayload to JSON: " + e.getMessage(), e);
        } catch (JMSRuntimeException e) {
            logger.errorf("Failed to send build message to UMB for tag: %s. Exception: %s",
                    payloadMessage.tag, e.getMessage());
            throw new BuildTriggerException("Failed to send build message to UMB due to internal error: " + e.getMessage(), e);
        }
    }
}
