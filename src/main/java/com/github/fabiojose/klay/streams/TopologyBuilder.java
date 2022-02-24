package com.github.fabiojose.klay.streams;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;

@ApplicationScoped
public class TopologyBuilder {

  private String from;
  private String to;
  private File script;

  @Produces
  public Topology build() {

    final var builder = new StreamsBuilder();
    final var stream = builder.stream("orders");

    return builder.build();
  }
}
