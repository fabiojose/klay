package com.github.fabiojose.kafka;

import io.quarkus.runtime.annotations.QuarkusMain;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@QuarkusMain
@Command(
  name = "klay",
  subcommands = {
    RunCommand.class,
    StartCommand.class
  }
)
public final class Klay {

  public static void main(String[] args) {
    new CommandLine(Klay.class).execute(args);
    //Quarkus.run(args);
  }

}
