package com.example.api.health;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class HealthEndpoints {

    @GET
    @Path("ping")
    public Response ping() {
        return Response
            .status(200)
            .entity("pong")
            .type(MediaType.TEXT_PLAIN_TYPE)
            .build();
    }

    @GET
    @Path("health")
    public Response health() {
        // TODO Check whether all components are up and running.
        return Response.ok().build();
    }
}
