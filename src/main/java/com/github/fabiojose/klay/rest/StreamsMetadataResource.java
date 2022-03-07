package com.github.fabiojose.klay.rest;

import com.github.fabiojose.klay.rest.model.KeyMetadata;
import com.github.fabiojose.klay.rest.model.StreamMetadata;
import com.github.fabiojose.klay.streams.TopologyBuilder;

import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyQueryMetadata;

@ApplicationScoped
@Path("/api/streams/v1/metadata")
public class StreamsMetadataResource {

  @Inject
  TopologyBuilder streams;

  @GET
  @Path("/all")
  @Produces(MediaType.APPLICATION_JSON)
  public Set<StreamMetadata> all() {
    return streams.getStreams()
      .metadataForAllStreamsClients()
      .stream()
      .map(StreamMetadata::of)
      .collect(Collectors.toSet());
  }

  /*TODO:
  @GET
  @Path("/metadata/local")
  @Produces(MediaType.APPLICATION_JSON)
  public void local() {
    streams.metadataForLocalThreads();
  }*/

  @GET
  @Path("/stores/{storeName}")
  @Produces(MediaType.APPLICATION_JSON)
  public Set<StreamMetadata> streamsMetadataForStore(
    final @PathParam("storeName") String storeName
  ) {
    return streams.getStreams()
      .streamsMetadataForStore(storeName)
      .stream()
      .map(StreamMetadata::of)
      .collect(Collectors.toSet());
  }

  @GET
  @Path("/stores/{storeName}/keys/{key}")
  public Response queryMetadataForKey(
    @PathParam("storeName") String storeName,
    @PathParam("key") String key
  ) {
    //TODO: Use the default.key.serde as key serializer
    var found = streams.getStreams().queryMetadataForKey(storeName, key, Serdes.String().serializer());

    if(KeyQueryMetadata.NOT_AVAILABLE != found){
      return Response.ok(KeyMetadata.of(found)).build();
    }

    return Response.status(Status.NOT_FOUND).build();
  }

  /*TODO:
  @GET
  @Path("/lag")
  @Produces(MediaType.APPLICATION_JSON)
  public void lag() {
    //streams.allLocalStorePartitionLags()
  }

  //TODO:
  @GET
  @Path("/metrics")
  @Produces(MediaType.APPLICATION_JSON)
  public void metrics() {
    streams.metrics();
  }*/
}
