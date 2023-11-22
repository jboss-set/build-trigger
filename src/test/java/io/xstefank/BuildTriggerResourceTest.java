package io.xstefank;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;
import io.xstefank.client.PKBClient;
import io.xstefank.model.json.BuildInfo;
import io.xstefank.model.json.BuildModifyInfo;
import io.xstefank.model.json.Environment;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.ws.rs.core.MediaType;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class BuildTriggerResourceTest {

    private static final String BUILD_INFO_PROJECT = "Test project";
    private static final String BUILD_INFO_UPSTREAM = "git+https://github.com/non-exisitent/repo.git";
    private static final String BUILD_INFO_VERSION = "1.0.0.Final";
    private static final String BUILD_INFO_REVISION = "61ec86095de795f5fb817a7cc824d8d7cfb9ae51";
    private static final String BUILD_INFO_SCRIPT = "mvn 'deploy'";
    private static final List<String> BUILD_INFO_ENVIRONMENT_OPENJDK = List.of("8");
    private static final List<String> BUILD_INFO_ENVIRONMENT_MAVEN = List.of("3.3.9");
    private static final String USER_EMAIL = "user@email.com";

    @InjectMock
    @RestClient
    PKBClient pkbClient;

    @InjectSpy
    BuildTrigger buildTrigger;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @TestSecurity(user = "user", roles = "user")
    @OidcSecurity(claims = {
        @Claim(key = "email", value = USER_EMAIL)
    })
    public void testTriggerEndpoint() throws Exception {
        Mockito.when(pkbClient.getProjects()).thenReturn(List.of());

        BuildInfo buildInfo = createTestBuildInfo();

        given()
            .body(objectMapper.writeValueAsString(buildInfo))
            .contentType(MediaType.APPLICATION_JSON)
            .when().post("/build-trigger/trigger")
            .then()
            .statusCode(200)
            .body(is("Triggered build for " + BUILD_INFO_PROJECT + ":" + BUILD_INFO_VERSION));

        Mockito.verify(buildTrigger, Mockito.times(1))
            .triggerBuild(Mockito.argThat(x -> {
                assertEquals(USER_EMAIL, x.email);
                assertEquals(BUILD_INFO_PROJECT, x.project);
                assertEquals(BUILD_INFO_UPSTREAM, x.upstream);
                assertEquals(BUILD_INFO_VERSION, x.version);
                assertEquals(BUILD_INFO_REVISION, x.revision);
                assertEquals(BUILD_INFO_ENVIRONMENT_OPENJDK, x.environment.openjdk);
                assertEquals(BUILD_INFO_ENVIRONMENT_MAVEN, x.environment.maven);
                assertEquals(BUILD_INFO_SCRIPT, x.script);
                return true;
            }));
    }

    @Test
    @TestSecurity(user = "user", roles = "user")
    @OidcSecurity(claims = {
        @Claim(key = "email", value = USER_EMAIL)
    })
    public void testModifyEndpoint() throws Exception {
        Mockito.when(pkbClient.getProjects()).thenReturn(List.of());

        BuildModifyInfo buildModifyInfo = createTestBuildModifyInfo();

        given()
            .body(objectMapper.writeValueAsString(buildModifyInfo))
            .contentType(MediaType.APPLICATION_JSON)
            .when().put("/build-trigger/modify")
            .then()
            .statusCode(200)
            .body(is("Triggered build modification for " + BUILD_INFO_PROJECT));

        Mockito.verify(buildTrigger, Mockito.times(1))
            .modifyBuild(Mockito.argThat(x -> {
                assertEquals(USER_EMAIL, x.email);
                assertEquals(BUILD_INFO_PROJECT, x.project);
                assertEquals(BUILD_INFO_UPSTREAM, x.upstream);
                assertEquals(BUILD_INFO_ENVIRONMENT_OPENJDK, x.environment.openjdk);
                assertEquals(BUILD_INFO_ENVIRONMENT_MAVEN, x.environment.maven);
                assertEquals(BUILD_INFO_SCRIPT, x.script);
                return true;
            }));
    }

    @Test
    @Disabled("Anonymous identity doesn't work in tests. (https://github.com/quarkusio/quarkus/issues/21888)")
    @TestSecurity
    public void testTriggerEndpointNoAuth() throws Exception {
        Mockito.when(pkbClient.getProjects()).thenReturn(List.of());

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

    private BuildInfo createTestBuildInfo() {
        BuildInfo buildInfo = new BuildInfo();
        buildInfo.project = BUILD_INFO_PROJECT;
        buildInfo.upstream = BUILD_INFO_UPSTREAM;
        buildInfo.version = BUILD_INFO_VERSION;
        buildInfo.revision = BUILD_INFO_REVISION;
        buildInfo.script = BUILD_INFO_SCRIPT;
        buildInfo.environment = new Environment();
        buildInfo.environment.openjdk = BUILD_INFO_ENVIRONMENT_OPENJDK;
        buildInfo.environment.maven = BUILD_INFO_ENVIRONMENT_MAVEN;

        return buildInfo;
    }

    private BuildModifyInfo createTestBuildModifyInfo() {
        BuildModifyInfo buildModifyInfo = new BuildModifyInfo();
        buildModifyInfo.project = BUILD_INFO_PROJECT;
        buildModifyInfo.upstream = BUILD_INFO_UPSTREAM;
        buildModifyInfo.script = BUILD_INFO_SCRIPT;
        buildModifyInfo.environment = new Environment();
        buildModifyInfo.environment.openjdk = BUILD_INFO_ENVIRONMENT_OPENJDK;
        buildModifyInfo.environment.maven = BUILD_INFO_ENVIRONMENT_MAVEN;

        return buildModifyInfo;
    }
}
