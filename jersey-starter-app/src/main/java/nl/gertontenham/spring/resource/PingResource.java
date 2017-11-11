package nl.gertontenham.spring.resource;

import nl.gertontenham.spring.resource.cache.EtagCachable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/ping")
@EtagCachable
public class PingResource {

    private static final Logger log = LoggerFactory.getLogger(PingResource.class);

    private String data = "pong!";

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @PermitAll
    public Response handlePing() {
        return Response.ok(data).build();
    }

    @POST
    @Consumes("text/plain")
    @RolesAllowed("user")
    public Response post(String data) {
        this.data = data;
        return Response.noContent().contentLocation(this.handlePing().getLocation()).build();
    }
}
