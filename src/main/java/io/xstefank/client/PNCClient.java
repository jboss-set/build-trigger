package io.xstefank.client;

import io.xstefank.pnc.model.json.EAP80BuildConfigs;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/pnc-rest/v2")
@RegisterRestClient(configKey = "pnc-client")
public interface PNCClient {

    String EAP_8_0_ID = "290";

    @GET
    @Path("/product-versions/" + EAP_8_0_ID + "/build-configs")
    EAP80BuildConfigs getEAP80Projects(@QueryParam("pageIndex") int pageIndex, @QueryParam("pageSize") int pageSize);
}
