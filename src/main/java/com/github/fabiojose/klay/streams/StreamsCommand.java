package com.github.fabiojose.klay.streams;

import com.github.fabiojose.klay.util.Utils;
import io.quarkus.runtime.Quarkus;
import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

@Command(name = "streams", mixinStandardHelpOptions = true)
public class StreamsCommand implements Runnable {

  private static final String PROPERTY_PREFIX = "kafka-streams.";

  @Parameters(
    paramLabel = "FILE",
    description = "Groovy file with Kafka Streams processing steps."
  )
  private File script;

  @Option(
    names = { "-p", "--property" },
    paramLabel = "NAME=VALUE",
    description = { "Kafka Streams configurations." },
    required = false
  )
  private Map<String, String> properties;

  @Option(
    names = { "--from" },
    description = "Topic name to create the stream's source.",
    required = true
  )
  private String from;

  @Option(
    names = { "--to" },
    description = "The stream's sink topic name.",
    required = false
  )
  private Optional<String> to;

  @Option(
    names = { "-s", "--bootstrap-servers" },
    paramLabel = "BROKER1:PORT,BROKER2:PORT",
    description = "Apache Kafka® cluster brokers.",
    defaultValue = "localhost:9092",
    showDefaultValue = Visibility.ALWAYS,
    required = true
  )
  private String bootstrapServers;

  @Option(
    names = { "-i", "--application-id" },
    paramLabel = "ID",
    description = "Kafka Streams application id.",
    required = true
  )
  private String applicationId;

  @Option(
    names = { "--server-port" },
    paramLabel = "PORT",
    description = {
      "Port to bind the Rest API Server.", "Default value will be dynamic",
    },
    required = false
  )
  private Optional<Integer> serverPort;

  @Spec
  private CommandSpec spec;

  private void validate() {
    //TODO: can read the script?

  }

  private void configure() {
    //TODO: Configure the Quarkus Kafka Streams properties

    System.setProperty("quarkus.kafka-streams.topics", from);

    //TODO: quarkus.kafka-streams.application-server

    //TODO: Bootstrap servers
    System.setProperty(
      "quarkus.kafka-streams.bootstrap-servers",
      bootstrapServers
    );

    //TODO: Application ID
    System.setProperty("quarkus.kafka-streams.application-id", applicationId);

    //TODO: Properties
    Optional
      .ofNullable(properties)
      .map(Map::entrySet)
      .stream()
      .flatMap(Set::stream)
      .map(kv -> Map.entry(PROPERTY_PREFIX + kv.getKey(), kv.getValue()))
      .forEach(p -> System.setProperty(p.getKey(), p.getValue()));

    //TODO: Configure properties: from, to and file
    System.setProperty("klay.stream.from.topic", from);
    to.ifPresent(topic -> System.setProperty("klay.stream.to.topic", topic));
    System.setProperty("klay.stream.script", script.getAbsolutePath());

    //TODO: Rest API server port
    System.setProperty(
      "quarkus.http.port",
      String.valueOf(serverPort.orElseGet(() -> Utils.freeTCPPort()))
    );
    //quarkus.kafka-streams.application-server=
  }

  @Override
  public void run() {
    validate();
    configure();

    // Start quarkus
    Quarkus.run(new String[] {});
  }
}
