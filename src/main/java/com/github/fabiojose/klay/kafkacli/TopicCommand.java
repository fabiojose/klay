package com.github.fabiojose.klay.kafkacli;

import com.github.fabiojose.klay.KafkaCLICommand;
import com.github.fabiojose.klay.util.MetadataWriter;
import com.github.fabiojose.klay.util.Utils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParentCommand;

@Command(
  name = TopicCommand.NAME,
  description = "Same as kafka-topics",
  requiredOptionMarker = '*'
)
public class TopicCommand implements Runnable {

  private TopicCommand() {}

  private static CommandSpec SPEC;
  public static final String NAME = "topic";

  @ParentCommand
  KafkaCLICommand parent;

  private void writeMetadata() {
    var writer = MetadataWriter.of(parent.getTopCommand().getExternalId());
    writer.type("topics");
    writer.version("3.1.0");
  }

  @Override
  public void run() {
    writeMetadata();

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
