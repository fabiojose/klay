package com.github.fabiojose.klay;

import com.github.fabiojose.klay.streams.StreamsBuildCommand;

import picocli.CommandLine.Command;

@Command(
  name = "build",
  description = "Build convenient uber-jar",
  mixinStandardHelpOptions = true,
  subcommands = {
    StreamsBuildCommand.class
  }
)
public class BuildCommand implements Runnable {

  @Override
  public void run() {

  }

}
