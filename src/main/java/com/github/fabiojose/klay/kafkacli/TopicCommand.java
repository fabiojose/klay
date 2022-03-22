package com.github.fabiojose.klay.kafkacli;

import com.github.fabiojose.klay.util.Utils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;

@Command(
  name = TopicCommand.NAME,
  description = "Same as kafka-topics",
  requiredOptionMarker = '*'
)
public class TopicCommand implements Runnable {

  private TopicCommand() {}

  private static CommandSpec SPEC;
  public static final String NAME = "topic";

  @Override
  public void run() {
    kafka.admin.TopicCommand.main(
      Utils.asArrayOfString(SPEC.commandLine().getUnmatchedArguments())
    );
  }

  public static final CommandSpec programmatic() {
    if (null == SPEC) {
      var command = new TopicCommand();
      SPEC = CommandSpec.forAnnotatedObject(command);
    }

    return SPEC;
  }
}
