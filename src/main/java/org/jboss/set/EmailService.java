package org.jboss.set;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import org.jboss.set.model.json.BuildJMSTriggerPayload;

import java.util.List;

@ApplicationScoped
public class EmailService {

    Logger logger = Logger.getLogger(EmailService.class);

    @Inject
    Mailer mailer;

    public void sendMail(BuildJMSTriggerPayload payload) {
        String subject = "Request to build project: " + payload.gitRepo + ", version: " + payload.projectVersion + " successfully sent";
        String text = "Request to build project: " + payload.gitRepo + ", version: " + payload.projectVersion
                + " trigger using Build-trigger tool successfully sent to a productization team.\n\n"
                + "Payload:\n" + payload + "\n\n\n"
                + "Report any issues or share suggestions using our GitHub Repository: https://github.com/jboss-set/build-trigger/issues";

        mailer.send(new Mail()
                .setSubject(subject)
                .setText(text)
                .setTo(List.of(payload.email)));

        logger.infof("Sending mail to %s about successful request to build project.\n Subject: %s\n Text: %s", payload.email, subject, text);
    }
}