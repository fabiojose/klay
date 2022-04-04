package com.github.fabiojose.klay.serialization;

import java.util.HashMap;

import io.quarkus.kafka.client.serialization.JsonbSerde;

@SuppressWarnings("rawtypes")
public class JsonSerde extends JsonbSerde<HashMap> {

  public JsonSerde() {
    super(HashMap.class);
  }

}
