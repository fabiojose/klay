package com.github.fabiojose.klay.rest.model;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.kafka.streams.KeyQueryMetadata;

public record KeyMetadata(Binding binding, int partition, Set<Binding> standby) {

  public static KeyMetadata of(KeyQueryMetadata metadata){
    return new KeyMetadata(
      Binding.of(metadata.activeHost()),
      metadata.partition(),
      metadata.standbyHosts().stream().map(Binding::of).collect(Collectors.toSet())
    );
  }
}
