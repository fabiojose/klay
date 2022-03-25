package com.github.fabiojose.klay.kafkacli;

import com.github.fabiojose.klay.KafkaCLICommand;
import com.github.fabiojose.klay.util.MetadataWriter;
import com.github.fabiojose.klay.util.Utils;
import kafka.tools.ConsoleConsumer;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;
import picocli.CommandLine.Model.CommandSpec;

@Command(name = "consumer", description = "Same as kafka-console-consumer")
public class ConsoleConsumerCommand implements Runnable {

  private ConsoleConsumerCommand() {}

  @ParentCommand
  KafkaCLICommand parent;

  private static CommandSpec SPEC;
  public static final String NAME = "consumer";

  public static CommandSpec programmatic() {
    if (null == SPEC) {
      var command = new ConsoleConsumerCommand();
      SPEC = CommandSpec.forAnnotatedObject(command);
    }

    return SPEC;
  }

  private void writeMetadata() {

    var writer = MetadataWriter.of(parent.getTopCommand().getExternalId());
    writer.type("consumer");
    writer.version("3.1.0");

  }

  @Override
  public void run() {
    writeMetadata();

    ConsoleConsumer.main(
      Utils.asArrayOfString(SPEC.commandLine().getUnmatchedArguments())
    );
  }
}
