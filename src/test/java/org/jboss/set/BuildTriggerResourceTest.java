package org.jboss.set;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;
import org.jboss.set.model.json.BuildInfo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.ws.rs.core.MediaType;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.jboss.set.model.json.Stream.EAP_7_3_X;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class BuildTriggerResourceTest {

    private static final String USER_EMAIL = "user@email.com";
    private static final String BUILD_INFO_TAG = "https://github.com/non-exisitent/repo/tree/1.0.0.Final.git";
    private static final String BUILD_INFO_GIT_REPO = "https://github.com/non-exisitent/repo.git";
    private static final String BUILD_INFO_PROJECT_VERSION = "1.0.0.Final";
    private static final String BUILD_INFO_COMMIT_SHA = "61ec86095de795f5fb817a7cc824d8d7cfb9ae51";
    private static final List<String> BUILD_INFO_STREAMS = List.of(EAP_7_3_X.frontEnd);
    private static final List<String> BUILD_JMS_TRIGGER_PAYLOAD_STREAMS = List.of(EAP_7_3_X.backEnd);

    @InjectSpy
    BuildTrigger buildTrigger;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @TestSecurity(user = "user", roles = "user")
    @OidcSecurity(claims = {
        @Claim(key = "email", value = USER_EMAIL)
    })
    public void testTriggerEndpoint() throws Exception {
        BuildInfo buildInfo = createTestBuildInfo();

        given()
            .body(objectMapper.writeValueAsString(buildInfo))
            .contentType(MediaType.APPLICATION_JSON)
            .when().post("/build-trigger/trigger")
            .then()
            .statusCode(200)
            .body(is("Build triggered successfully for repository " + BUILD_INFO_GIT_REPO + ", version: " + BUILD_INFO_PROJECT_VERSION));

        Mockito.verify(buildTrigger, Mockito.times(1))
            .triggerBuild(Mockito.argThat(x -> {
                assertEquals(USER_EMAIL, x.email);
                assertEquals(BUILD_INFO_TAG, x.tag);
                assertEquals(BUILD_INFO_GIT_REPO, x.gitRepo);
                assertEquals(BUILD_INFO_PROJECT_VERSION, x.projectVersion);
                assertEquals(BUILD_INFO_COMMIT_SHA, x.commitSha);
                assertEquals(BUILD_JMS_TRIGGER_PAYLOAD_STREAMS, x.streams);
                return true;
            }));
    }

    @Test
    @Disabled("Anonymous identity doesn't work in tests. (https://github.com/quarkusio/quarkus/issues/21888)")
    @TestSecurity
    public void testTriggerEndpointNoAuth() throws Exception {
        BuildInfo buildInfo = createTestBuildInfo();

        given()
            .header("Authorization", "")
            .body(objectMapper.writeValueAsString(buildInfo))
            .contentType(MediaType.APPLICATION_JSON)
            .when().post("/build-trigger/trigger")
            .then()
            .log().all()
            .statusCode(403);
    }

    @Test
    @TestSecurity(user = "user", roles = "user")
    @OidcSecurity(claims = {
            @Claim(key = "email", value = USER_EMAIL)
    })
    public void testTriggerEndpointInvalidStream() throws Exception {
        BuildInfo buildInfo = createTestBuildInfo().toBuilder()
                .streams(List.of("Invalid"))
                .build();

        given()
                .body(objectMapper.writeValueAsString(buildInfo))
                .contentType(MediaType.APPLICATION_JSON)
                .when().post("/build-trigger/trigger")
                .then()
                .statusCode(422);
    }

    private BuildInfo createTestBuildInfo() {
        return new BuildInfo.Builder()
                .tag(BUILD_INFO_TAG)
                .gitRepo(BUILD_INFO_GIT_REPO)
                .projectVersion(BUILD_INFO_PROJECT_VERSION)
                .commitSha(BUILD_INFO_COMMIT_SHA)
                .streams(BUILD_INFO_STREAMS)
                .build();
    }
}
