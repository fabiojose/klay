package com.github.fabiojose.klay.rest.model;

import org.apache.kafka.streams.state.HostInfo;

/**
 * Host and port biding
 */
public record Binding(String host, int port) {

  /**
   * Creates new instance of {@link Binding} based on {@link HostInfo}
   */
  public static Binding of(HostInfo hi){
    return new Binding(hi.host(), hi.port());
  }

}
