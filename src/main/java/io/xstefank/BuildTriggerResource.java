package io.xstefank;

import io.xstefank.model.json.BuildInfo;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class BuildTriggerResource {

    @Inject
    BuildTrigger buildTrigger;

    @POST
    @Path("/trigger")
    public Response triggerBuildFromUI(BuildInfo buildInfo) {
        System.out.println(buildInfo);

        return Response.ok("Triggered build for " + buildInfo.product.name + ":" + buildInfo.product.version).build();
    }

    @GET
    @Path("/umb")
    public void testUmb() {
        buildTrigger.triggerBuild(null);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        String testJson = "{\n" +
            "  \"upstream\": \"https://github.com/rh-messaging/artemis-wildfly-integration\",\n" +
            "  \"revision\": \"03ebee9204785a8ea078b0e5cf1de54deb317167\",\n" +
            "  \"script\": \"mvn '-P release' '-Dinsecure.repositories=WARN' '-DperformRelease' 'deploy'\",\n" +
            "  \"version\": \"1.0.4\",\n" +
            "  \"product\": {\n" +
            "    \"version\": \"7.3.9\",\n" +
            "    \"name\": \"eap\"\n" +
            "  },\n" +
            "  \"environment\" : {\n" +
            "    \"openjdk\" : [\"8\"],\n" +
            "    \"maven\" : [\"3.3.9\"]\n" +
            "  }\n" +
            "}\n";

        final Jsonb jsonb = JsonbBuilder.newBuilder().build();
        final BuildInfo testMessage = jsonb.fromJson(testJson, BuildInfo.class);
        System.out.println(jsonb.toJson(testMessage));

        System.out.println("Sending message " );
//        sendTestMessage();

        return "Message sent";
    }
}
