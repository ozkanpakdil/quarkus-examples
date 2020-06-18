package com.mascix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import io.reactivex.Single;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.mutiny.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.templ.freemarker.FreeMarkerTemplateEngine;

@Path("/")
public class GreetingResource {
    @Inject
    Vertx vertx;

    @Context
    HttpServerRequest request;

    private HashMap data;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Uni<Object> doSomethingAsync() throws Exception {
        FreeMarkerTemplateEngine engine = getEngineReady();
        Single<Buffer> page = engine.rxRender(this.data, "/templates/index.htm");
        return Uni.createFrom().item(page.toFuture().get().toString());
        //  return Uni.createFrom().converter(UniRxConverters.fromSingle(),engine.rxRender(data, "/templates/index.htm"));
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
        data.put("request", request);
        return data;
    }

    private FreeMarkerTemplateEngine getEngineReady() {
        this.data = createSampleData();
        var params = vertx.getDelegate();
        var args = new io.vertx.reactivex.core.Vertx(params);
        FreeMarkerTemplateEngine engine = FreeMarkerTemplateEngine.create(args);

        return engine;
    }
}