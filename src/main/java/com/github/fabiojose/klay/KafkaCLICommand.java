package com.github.fabiojose.klay;

import com.github.fabiojose.klay.kafkacli.TopicCommand;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;

@Command(
  name = "kafka-cli",
  aliases = {"k", "kc", "cli"},
  description = "Wraps the official Apache Kafka® CLI for convenience",
  mixinStandardHelpOptions = true
)
public class KafkaCLICommand implements Runnable {

  private KafkaCLICommand(){}

  @Override
  public void run() {
  }

  public static CommandSpec programmatic() {

    final var command = new KafkaCLICommand();
    final var spec = CommandSpec.forAnnotatedObject(command);

    // add programmatic created commands
    spec.addSubcommand(TopicCommand.NAME, TopicCommand.programmatic());

    return spec;
  }
}
