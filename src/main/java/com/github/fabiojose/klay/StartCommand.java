package com.github.fabiojose.klay;

import com.github.fabiojose.klay.broker.BrokerCommand;
import com.github.fabiojose.klay.streams.StreamsCommand;
import com.github.fabiojose.klay.util.MetadataWriter;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

@Command(
  name = "start",
  subcommands = {
    BrokerCommand.class,
    StreamsCommand.class
  },
  mixinStandardHelpOptions = true
)
public class StartCommand implements Runnable {

  @ParentCommand
  Klay parent;

  private void writeMetadata() {

    var metadata = MetadataWriter.of(parent.getExternalId());

    //pid
    metadata.pid(ProcessHandle.current().pid());

  }

  @Override
  public void run() {
    writeMetadata();
  }

  public Klay getTopCommand() {
    return parent;
  }
}
