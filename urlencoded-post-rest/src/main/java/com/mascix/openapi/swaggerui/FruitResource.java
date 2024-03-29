package com.mascix.openapi.swaggerui;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

@Path("/fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FruitResource {

  private Set<Fruit> fruits = Collections.newSetFromMap(
    Collections.synchronizedMap(new LinkedHashMap<>())
  );
  private FruitResource service;

  public FruitResource() {
    fruits.add(new Fruit("Apple", "Winter fruit"));
    fruits.add(new Fruit("Pineapple", "Tropical fruit"));
  }

  @GET
  public Set<Fruit> list() {
    return fruits;
  }

//   @POST
//   public Set<Fruit> add(Fruit fruit) {
//     fruits.add(fruit);
//     return fruits;
//   }

  @DELETE
  public Set<Fruit> delete(Fruit fruit) {
    fruits.removeIf(existingFruit ->
      existingFruit.name.contentEquals(fruit.name)
    );
    return fruits;
  }

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response addItems(
    @Parameter(description = "Items to add") @QueryParam(
      "items"
    ) List<Long> items
  ) {
    return service.addItems(items);
  }
}
