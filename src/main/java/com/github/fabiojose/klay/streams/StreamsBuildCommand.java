package com.github.fabiojose.klay.streams;

import picocli.CommandLine.Command;

@Command(
  name = "streams",
  description = "Build uber-jar with your stream topology ready to execute",
  mixinStandardHelpOptions = true
)
public class StreamsBuildCommand implements Runnable {

  @Override
  public void run() {

  }

}
