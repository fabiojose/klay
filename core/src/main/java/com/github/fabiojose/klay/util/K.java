package com.github.fabiojose.klay.util;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;

/**
 * Interface to be implmented by topology builders written in Java.
 */
@FunctionalInterface
public interface K {

  @SuppressWarnings("rawtypes")
  KStream build(KStream fromStream, StreamsBuilder builder);

}
