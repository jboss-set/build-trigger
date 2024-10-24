package org.jboss.set;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.set.model.json.BuildJMSTriggerPayload;

import java.util.List;

@ApplicationScoped
public class EmailService {

    @Inject
    Mailer mailer;

    public void sendMail(BuildJMSTriggerPayload payload) {
        String project = payload.gitRepo.substring(payload.gitRepo.lastIndexOf('/') + 1);
        String subject = "[Build Trigger] Build of the " + capitalizeFirstLetter(project) + " (version " + payload.projectVersion + ") requested";
        String htmlText = "<p>Request to build <b>" + payload.gitRepo + "</b> in version <b>" + payload.projectVersion + "</b> "
                + "was successfully sent to the productization team using the following parameters:</p>"
                + "<ul>"
                + "<li><b>Tag:</b> " + payload.tag + "</li>"
                + "<li><b>Repository:</b> " + payload.gitRepo + "</li>"
                + "<li><b>Version:</b> " + payload.projectVersion + "</li>"
                + "<li><b>Commit:</b> " + payload.commitSha + "</li>"
                + "<li><b>Streams:</b> " + payload.streams + "</li>"
                + "</ul>"
                + "<p>Report any issues or share suggestions using our "
                + "<a href=\"https://github.com/jboss-set/build-trigger/issues\">GitHub Repository</a>.</p>"
                + "<p>This is an automated Build Trigger message, please do not respond!</p>";


        mailer.send(new Mail()
                .setSubject(subject)
                .setHtml(htmlText)
                .setTo(List.of(payload.email)));
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }
}