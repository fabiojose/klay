package com.github.fabiojose.klay;

import com.github.fabiojose.klay.streams.StreamsCommand;

import picocli.CommandLine.Command;

@Command(
  name = "run",
  subcommands = {
    StreamsCommand.class
  }
)
public class RunCommand {

}
