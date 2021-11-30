package io.xstefank;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.quarkus.test.security.TestSecurity;
import io.xstefank.client.PKBClient;
import io.xstefank.model.json.BuildInfo;
import io.xstefank.model.json.Environment;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.MediaType;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class BuildTriggerResourceTest {

    @InjectMock
    @RestClient
    PKBClient pkbClient;

    @InjectSpy
    BuildTrigger buildTrigger;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @TestSecurity(authorizationEnabled = false)
    public void testHelloEndpoint() throws Exception {
        Mockito.when(pkbClient.getProjects()).thenReturn(List.of());

        BuildInfo buildInfo = new BuildInfo();
        buildInfo.project = "Test project";
        buildInfo.upstream = "git+https://github.com/non-exisitent/repo.git";
        buildInfo.version = "1.0.0.Final";
        buildInfo.revision = "ASDFGASGASDG23534252345234";
        buildInfo.product = List.of("EAP:7.3.x", "EAP:7.4.x");
        Environment environment = new Environment();
        environment.openjdk = List.of("8");
        environment.maven = List.of("3.3.9");
        buildInfo.environment = environment;
        buildInfo.script = "mvn 'deploy'";


        given()
            .body(objectMapper.writeValueAsString(buildInfo))
            .contentType(MediaType.APPLICATION_JSON)
            .when().post("/build-trigger/trigger")
            .then()
            .statusCode(200)
            .body(is("Triggered build for Test project:1.0.0.Final"));
    }

}
