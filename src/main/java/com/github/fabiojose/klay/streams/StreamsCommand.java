package com.github.fabiojose.klay.streams;

import java.io.File;
import java.util.Map;

import io.quarkus.runtime.Quarkus;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

@Command(
  name = "streams",
  mixinStandardHelpOptions = true
)
public class StreamsCommand implements Runnable {

  @Parameters(
    paramLabel = "FILE",
    description = "Groovy file with Kafka Streams processing steps."
  )
  private File script;

  @Option(
    names = {"-p", "--property"},
    paramLabel = "NAME=VALUE",
    description = {
      "Kafka Streams configurations."
    },
    required = false
  )
  private Map<String, String> properties;

  @Option(
    names = {"--from"},
    description = "Topic name to create the stream's source.",
    required = true
  )
  private String from;

  @Option(
    names = {"--to"},
    description = "The stream's sink topic name.",
    required = true
  )
  private String to;

  @Option(
    names = {"-s", "--bootstrap-servers"},
    paramLabel = "BROKER1:PORT,BROKER2:PORT",
    description = "Apache Kafka® cluster brokers.",
    defaultValue = "localhost:9092",
    required = true
  )
  private String bootstrapServers;

  @Spec
  CommandSpec spec;

  @Override
  public void run() {
    System.err.println(properties.size());
    Quarkus.run(new String[]{});
  }

}
