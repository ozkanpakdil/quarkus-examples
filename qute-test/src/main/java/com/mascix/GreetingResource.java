package com.mascix;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateExtension;
import io.quarkus.qute.TemplateInstance;

@Path("/")
public class GreetingResource {

    @Inject
    ItemService service;

    @Inject
    Template index; 

    
    @GET
    @Path("{id}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get(@PathParam("id") Integer id) {
        return index.data("item", service.findItem(id)); 
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index() {

        return index.data("item", service.findItem(null)); 
    }

    @TemplateExtension 
    static BigDecimal discountedPrice(Item item) {
        return item.price.multiply(new BigDecimal("0.9"));
    }
}