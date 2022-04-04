package com.github.fabiojose.klay.rest.model;

import org.apache.kafka.common.TopicPartition;

public record TopicAndPartition(String topic, int partition) {

  public static TopicAndPartition of(TopicPartition tp) {
    return new TopicAndPartition(tp.topic(), tp.partition());
  }

}
