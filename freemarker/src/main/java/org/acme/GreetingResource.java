package org.acme;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.quarkiverse.freemarker.TemplatePath;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("/hello")
public class GreetingResource {

    @Inject
    @TemplatePath("hello.ftl")
    Template hello;

    @GET
    @Produces(TEXT_PLAIN)
    public String hello(@QueryParam("name") String name) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        hello.process(Map.of("name", name), stringWriter);
        return stringWriter.toString();
    }
}