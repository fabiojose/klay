package com.github.fabiojose.klay.rest.model;

import org.apache.kafka.streams.state.HostInfo;

public record Binding(String host, int port) {

  public static Binding of(HostInfo hi){
    return new Binding(hi.host(), hi.port());
  }

}
