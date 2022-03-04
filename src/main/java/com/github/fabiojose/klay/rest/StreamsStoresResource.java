package com.github.fabiojose.klay.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;

@ApplicationScoped
@Path("/api/streams/v1/stores")
public class StreamsStoresResource {

  @Inject
  KafkaStreams streams;

  @GET
  @Path("/{storeName}/keys/{key}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dummy(
    @PathParam("storeName") String storeName,
    @PathParam("key") String key
  ) {

    final var store = streams.store(
      StoreQueryParameters.fromNameAndType(
        storeName,
        QueryableStoreTypes.keyValueStore()
      )
    );

    final var found = store.get(key);
    if (null != found) {
      return Response.ok(found).build();
    }

    return Response.status(Status.NOT_FOUND).build();
  }
}
