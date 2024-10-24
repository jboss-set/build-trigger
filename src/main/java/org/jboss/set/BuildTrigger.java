package org.jboss.set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSRuntimeException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Session;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.jboss.set.exception.BuildTriggerException;
import org.jboss.set.model.json.BuildJMSTriggerPayload;

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
            logger.infof("Build message sent to UMB. Payload: %s", payloadMessage);
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
            logger.errorf("Build message failed to be sent to UMB. Payload: %s. Exception: %s",
                    payloadMessage, e.getMessage());
            throw new BuildTriggerException("Build message failed to be sent to UMB due to: " + e.getMessage(), e);
        }
    }
}
