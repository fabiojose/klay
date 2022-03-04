package com.github.fabiojose.klay.rest.model;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.kafka.streams.StreamsMetadata;

public record StreamMetadata(Binding binding, Set<String> stores, Set<TopicAndPartition> partitions) {

  public static StreamMetadata of(StreamsMetadata sm) {
    return new StreamMetadata(
      Binding.of(sm.hostInfo()),
      sm.stateStoreNames(),
      sm.topicPartitions()
        .stream()
        .map(TopicAndPartition::of)
        .collect(Collectors.toSet())
      );
  }

}
