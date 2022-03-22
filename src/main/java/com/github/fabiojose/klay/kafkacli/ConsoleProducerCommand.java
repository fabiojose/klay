package com.github.fabiojose.klay.kafkacli;

import com.github.fabiojose.klay.util.Utils;
import kafka.tools.ConsoleProducer;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;

@Command(
  name = ConsoleProducerCommand.NAME,
  description = "Same as kafka-console-producer"
)
public class ConsoleProducerCommand implements Runnable {

  private ConsoleProducerCommand() {}

  private static CommandSpec SPEC;
  public static final String NAME = "producer";

  public static CommandSpec programmatic() {
    if (null == SPEC) {
      var command = new ConsoleProducerCommand();
      SPEC = CommandSpec.forAnnotatedObject(command);
    }

    return SPEC;
  }

  @Override
  public void run() {
    ConsoleProducer.main(
      Utils.asArrayOfString(SPEC.commandLine().getUnmatchedArguments())
    );
  }
}
