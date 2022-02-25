package com.github.fabiojose.klay;

import com.github.fabiojose.klay.broker.BrokerCommand;

import picocli.CommandLine.Command;

@Command(
  name = "start",
  subcommands = {
    BrokerCommand.class
  }
)
public class StartCommand {

}
