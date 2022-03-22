package com.github.fabiojose.klay.streams;

import com.github.fabiojose.klay.StartCommand;
import com.github.fabiojose.klay.util.MetadataWriter;
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
import picocli.CommandLine.ParentCommand;
import picocli.CommandLine.Spec;

@Command(name = "streams", mixinStandardHelpOptions = true)
public class StreamsCommand implements Runnable {

  private static final String PROPERTY_PREFIX = "kafka-streams.";

  @ParentCommand
  StartCommand parent;

  @Parameters(
    paramLabel = "FILE",
    description = "Groovy or Java file with Kafka Streams processing steps."
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

  @Option(
    names = {"--live"},
    description = "Live reload the topology.",
    defaultValue = "false",
    required = false
  )
  private Boolean liveReload;

  @Spec
  private CommandSpec spec;

  private void validate() {
    //TODO: can read the script?

  }

  private void configure() {

    System.setProperty("quarkus.kafka-streams.topics", from);

    // Bootstrap servers
    System.setProperty(
      "kafka-streams.bootstrap.servers",
      bootstrapServers
    );

    // Application ID
    System.setProperty("kafka-streams.application.id", applicationId);

    Optional
      .ofNullable(properties)
      .map(Map::entrySet)
      .stream()
      .flatMap(Set::stream)
      .map(kv -> Map.entry(PROPERTY_PREFIX + kv.getKey(), kv.getValue()))
      .forEach(p -> System.setProperty(p.getKey(), p.getValue()));

    // Configure properties: from, to and file
    System.setProperty("klay.stream.from.topic", from);
    to.ifPresent(topic -> System.setProperty("klay.stream.to.topic", topic));
    System.setProperty("klay.stream.script", script.getAbsolutePath());

    // Rest API server port
    System.setProperty(
      "quarkus.http.port",
      String.valueOf(serverPort.orElseGet(() -> Utils.freeTCPPort()))
    );

    System.setProperty("klay.stream.live", String.valueOf(liveReload));

    System.setProperty("klay.external-id", parent.getTopCommand().getExternalId());
  }

  private void writeMetadata() {

    var writer = MetadataWriter.of(parent.getTopCommand().getExternalId());

    // application.server, if configured
    if(null!= properties && properties.containsKey("application.server")){
      writer.ports("server=" + properties.get("application.server"));
    }

    // http port
    writer.ports(",rest=localhost:" + System.getProperty("quarkus.http.port"));

  }

  @Override
  public void run() {
    validate();
    configure();
    writeMetadata();

    // Start quarkus
    Quarkus.run(new String[] {});
  }
}
