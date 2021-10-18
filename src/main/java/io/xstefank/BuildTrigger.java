package io.xstefank;

import io.xstefank.model.json.BuildInfo;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Session;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

@ApplicationScoped
public class BuildTrigger {

    @ConfigProperty(name = "build-trigger.topic")
    String topic;

    @ConfigProperty(name = "quarkus.qpid-jms.url")
    String url;

    @Inject
    ConnectionFactory connectionFactory;

    Jsonb jsonb = JsonbBuilder.create();

    public void triggerBuild(BuildInfo payloadMessage) {
        try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
            context.createProducer().send(context.createTopic(topic), jsonb.toJson(payloadMessage));
        }
    }
}
