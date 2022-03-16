package com.github.fabiojose.klay;

import com.github.fabiojose.klay.broker.BrokerCommand;
import com.github.fabiojose.klay.streams.StreamsCommand;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

@Command(
  name = "start",
  description = "Start new executions, like: Broker, Kafka Streams",
  subcommands = {
    BrokerCommand.class,
    StreamsCommand.class
  },
  mixinStandardHelpOptions = true
)
public class StartCommand implements Runnable {

  @ParentCommand
  Klay parent;

  @Override
  public void run() {
  }

  public Klay getTopCommand() {
    return parent;
  }
}
