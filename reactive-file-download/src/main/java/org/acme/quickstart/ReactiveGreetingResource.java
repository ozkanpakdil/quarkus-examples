package org.acme.quickstart;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

@Path("hello")
@Slf4j
public class ReactiveGreetingResource {

    @GET
    @Path("/f/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Uni<Response> getFile(@PathParam String fileName) {
        File nf = new File(fileName);
        log.info("file:" + nf.exists());
        ResponseBuilder response = Response.ok((Object) nf);
        response.header("Content-Disposition", "attachment;filename=" + nf);
        Uni<Response> re = Uni.createFrom().item(response.build());
        return re;
    }

    @GET
    @Path("/list/{dir}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Set<String>> listFilesUsingJavaIO(@PathParam String dir) {
        log.info("dir:" + dir);
        File f = new File(dir);
        if (f.exists()) {
            Set<String> collect = Stream.of(new File(dir).listFiles()).filter(file -> !file.isDirectory())
                    .map(File::getName).collect(Collectors.toSet());
            return Uni.createFrom().item(collect);
        } else
            return Uni.createFrom().item(Stream.of(new File(".").listFiles()).filter(file -> !file.isDirectory())
                    .map(File::getName).collect(Collectors.toSet()));
    }
}