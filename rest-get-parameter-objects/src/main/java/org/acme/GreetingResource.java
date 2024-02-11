package org.acme;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.annotation.Nullable;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/hello")
public class GreetingResource {

    @GET
    public Response hello(@Nullable @BeanParam Person p) {
        return Response.ok(new HelloPerson(p)).build();

    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class Person {
        @QueryParam("name")
        String name;
    }

    @RegisterForReflection
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public class HelloPerson {
        Person person;

        public HelloPerson(Person p) {
            this.person = p;
        }
    }
}
