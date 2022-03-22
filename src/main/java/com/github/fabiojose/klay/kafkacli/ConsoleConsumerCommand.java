package com.github.fabiojose.klay.kafkacli;

import com.github.fabiojose.klay.util.Utils;
import kafka.tools.ConsoleConsumer;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;

@Command(name = "consumer", description = "Same as kafka-console-consumer")
public class ConsoleConsumerCommand implements Runnable {

  private ConsoleConsumerCommand() {}

  private static CommandSpec SPEC;
  public static final String NAME = "consumer";

  public static CommandSpec programmatic() {
    if (null == SPEC) {
      var command = new ConsoleConsumerCommand();
      SPEC = CommandSpec.forAnnotatedObject(command);
    }

    return SPEC;
  }

  @Override
  public void run() {
    ConsoleConsumer.main(
      Utils.asArrayOfString(SPEC.commandLine().getUnmatchedArguments())
    );
  }
}
