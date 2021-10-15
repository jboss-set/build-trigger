package io.xstefank;

import io.xstefank.model.json.BuildInfo;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Session;

@ApplicationScoped
public class BuildTrigger {

    @ConfigProperty(name = "topic")
    String topic;

    @ConfigProperty(name = "quarkus.qpid-jms.url")
    String url;

    @Inject
    ConnectionFactory connectionFactory;

    public void triggerBuild(BuildInfo payloadMessage) {
        System.out.println(topic);
        System.out.println(url);
        try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
            context.createProducer().send(context.createTopic(topic), "test message from build trigger app");
        }
    }
}
