package com.github.fabiojose.klay.broker;

import java.io.File;
import java.util.Map;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Help.Visibility;

@Command(
  name = "broker",
  mixinStandardHelpOptions = true
)
public class BrokerCommand {

  @Option(
    names = {"-p", "--property"},
    paramLabel = "NAME=VALUE",
    description = {
      "Apache Kafka® server configurations.",
      "That overrides the values in the --properties-file"
    },
    required = false
  )
  private Map<String, String> properties;

  @Option(
    names = {"--properties-file"},
    paramLabel = "FILE",
    description = {
      "File with Apache Kafka® server configurations."
    },
    required = false
  )
  private File propertiesFile;

  @Option(
    names = {"-z", "--zookeeper"},
    negatable = true,
    defaultValue = "true",
    showDefaultValue = Visibility.ALWAYS,
    required = false
  )
  private boolean start;

  static class PropertiesOptionGroup {
    Map<String, String> property;
    File file;
  }
}
