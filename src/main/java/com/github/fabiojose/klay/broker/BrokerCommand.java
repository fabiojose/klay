package com.github.fabiojose.klay.broker;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import com.github.fabiojose.klay.StartCommand;
import com.github.fabiojose.klay.util.Utils;

import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name = "broker", mixinStandardHelpOptions = true)
public class BrokerCommand implements Runnable {

  @ParentCommand
  StartCommand parent;

  @Option(
    names = { "-p", "--property" },
    paramLabel = "NAME=VALUE",
    description = {
      "Apache Kafka® server configurations.",
      "That overrides the values in the --properties-file",
    },
    required = false
  )
  Map<String, String> properties;

  @Option(
    names = { "--properties-file" },
    paramLabel = "FILE",
    description = { "File with Apache Kafka® server configurations." },
    required = false
  )
  Optional<File> propertiesFile;

  @Option(
    names = {"--no-zookeeper" },
    negatable = true,
    showDefaultValue = Visibility.ALWAYS,
    required = false
  )
  boolean noZookeeper;

  private Properties brokerProperties() {

    final var properties = propertiesFile
      .map(File::getAbsolutePath)
      .map(Utils::propertiesOf)
      .orElseGet(()-> new Properties());

    properties.putAll(
      Optional.ofNullable(this.properties)
        .orElseGet(() -> Map.of())
    );

    return properties;
  }

  @Override
  public void run() {

    final var startApacheKafka = new StartApacheKafka(
      !this.noZookeeper,
      new Properties(),
      brokerProperties(),
      parent.getTopCommand().getExternalId()
    );

    startApacheKafka.start();
  }
}
