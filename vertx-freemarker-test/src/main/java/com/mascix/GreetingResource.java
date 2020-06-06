package com.mascix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.reactivex.ext.web.templ.freemarker.FreeMarkerTemplateEngine;

@Path("/")
public class GreetingResource {
    @Inject
    Vertx vertx;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Uni<Object> doSomethingAsync() throws Exception {
        FreeMarkerTemplateEngine engine = getEngineReady();
        HashMap data = createSampleData();
        String page = engine.rxRender(data, "/templates/index.htm").toFuture().get().toString();
        return Uni.createFrom().item(page);
    }

    private HashMap createSampleData() {
        List<ExampleObject> exs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            exs.add(new ExampleObject("name:" + i, "dev:" + i));
        }
        HashMap data = new HashMap<>();
        data.put("title", "Vert.x Web");
        data.put("exampleObject", new ExampleObject("name", "dev"));
        data.put("systems", exs);
        return data;
    }

    private FreeMarkerTemplateEngine getEngineReady() {
        io.vertx.core.Vertx params = vertx.getDelegate();
        io.vertx.reactivex.core.Vertx args = new io.vertx.reactivex.core.Vertx(params);
        FreeMarkerTemplateEngine engine = FreeMarkerTemplateEngine.create(args);

        return engine;
    }
}