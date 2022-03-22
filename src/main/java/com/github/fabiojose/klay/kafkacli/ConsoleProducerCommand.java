package com.github.fabiojose.klay.kafkacli;

import com.github.fabiojose.klay.KafkaCLICommand;
import com.github.fabiojose.klay.util.MetadataWriter;
import com.github.fabiojose.klay.util.Utils;
import kafka.tools.ConsoleProducer;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;
import picocli.CommandLine.Model.CommandSpec;

@Command(
  name = ConsoleProducerCommand.NAME,
  description = "Same as kafka-console-producer"
)
public class ConsoleProducerCommand implements Runnable {

  private ConsoleProducerCommand() {}

  private static CommandSpec SPEC;
  public static final String NAME = "producer";

  @ParentCommand
  KafkaCLICommand parent;

  public static CommandSpec programmatic() {
    if (null == SPEC) {
      var command = new ConsoleProducerCommand();
      SPEC = CommandSpec.forAnnotatedObject(command);
    }

    return SPEC;
  }

  private void writeMetadata() {

    var writer = MetadataWriter.of(parent.getTopCommand().getExternalId());
    writer.type("producer");
    writer.version("3.1.0");

  }

  @Override
  public void run() {

    writeMetadata();

    ConsoleProducer.main(
      Utils.asArrayOfString(SPEC.commandLine().getUnmatchedArguments())
    );
  }
}
