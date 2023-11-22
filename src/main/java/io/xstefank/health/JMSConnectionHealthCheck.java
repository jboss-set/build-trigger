package io.xstefank.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import jakarta.inject.Inject;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;

@Readiness
public class JMSConnectionHealthCheck implements HealthCheck {

    private static final String HC_NAME = JMSConnectionHealthCheck.class.getSimpleName();

    @Inject
    ConnectionFactory connectionFactory;

    @Override
    public HealthCheckResponse call() {
        try (Connection connection = connectionFactory.createConnection()) {
            connection.start();
            connection.stop();
            return HealthCheckResponse.up(HC_NAME);
        } catch (JMSException e) {
            return HealthCheckResponse.builder()
                .name(HC_NAME)
                .down()
                .withData("exception", e.toString())
                .build();
        }
    }
}
