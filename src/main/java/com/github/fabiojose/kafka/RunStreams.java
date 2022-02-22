package com.github.fabiojose.kafka;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;

@ApplicationScoped
public class RunStreams {

  @Produces
  public Topology build() {

    final var builder = new StreamsBuilder();
    final var stream = builder.stream("orders");

    return builder.build();
  }
}
