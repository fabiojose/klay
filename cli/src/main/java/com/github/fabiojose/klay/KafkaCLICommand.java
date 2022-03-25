package com.github.fabiojose.klay;

import com.github.fabiojose.klay.kafkacli.ConsoleConsumerCommand;
import com.github.fabiojose.klay.kafkacli.ConsoleProducerCommand;
import com.github.fabiojose.klay.kafkacli.TopicCommand;
import com.github.fabiojose.klay.util.MetadataWriter;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;
import picocli.CommandLine.Model.CommandSpec;

@Command(
  name = "kafka-cli",
  aliases = { "k", "kc", "cli" },
  description = "Wraps the official Apache Kafka® CLI for convenience",
  mixinStandardHelpOptions = true
)
public class KafkaCLICommand implements Runnable {

  private KafkaCLICommand() {}

  @ParentCommand
  Klay parent;

  private void writeMetadata(){

    var writer = MetadataWriter.of(parent.getExternalId());
    writer.ports("none");

  }

  @Override
  public void run() {
    writeMetadata();
  }

  public Klay getTopCommand() {
    return parent;
  }

  public static CommandSpec programmatic() {
    final var command = new KafkaCLICommand();
    final var spec = CommandSpec.forAnnotatedObject(command);

    // add programmatic created commands
    spec
      .addSubcommand(TopicCommand.NAME, TopicCommand.programmatic())
      .addSubcommand(
        ConsoleConsumerCommand.NAME,
        ConsoleConsumerCommand.programmatic()
      )
      .addSubcommand(
        ConsoleProducerCommand.NAME,
        ConsoleProducerCommand.programmatic()
      );

    return spec;
  }
}
