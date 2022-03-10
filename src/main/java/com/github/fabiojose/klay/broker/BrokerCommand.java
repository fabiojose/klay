package com.github.fabiojose.klay.broker;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.Option;

@Command(name = "broker", mixinStandardHelpOptions = true)
public class BrokerCommand implements Runnable {

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
  File propertiesFile;

  @Option(
    names = {"--no-zookeeper" },
    negatable = true,
    showDefaultValue = Visibility.ALWAYS,
    required = false
  )
  boolean noZookeeper;

  static class PropertiesOptionGroup {

    Map<String, String> property;
    File file;
  }

  @Override
  public void run() {
    final var zookeeperOverrideProperties = new Properties();
    final var brokerOverrideProperties = new Properties();
    final var startApacheKafka = new StartApacheKafka(
      !this.noZookeeper,
      zookeeperOverrideProperties,
      brokerOverrideProperties
    );

    startApacheKafka.start();
  }
}
