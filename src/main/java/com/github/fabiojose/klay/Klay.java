package com.github.fabiojose.klay;

import com.github.fabiojose.klay.util.HoldCommands;

import io.quarkus.runtime.annotations.QuarkusMain;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@QuarkusMain
@Command(
  name = "klay",
  subcommands = {
    StartCommand.class,
    HoldCommands.PsCommand.class,
    HoldCommands.StopCommand.class,
    HoldCommands.LogsCommand.class
  },
  mixinStandardHelpOptions = true,
  usageHelpWidth = 100
)
public final class Klay implements Runnable {

  @Option(
    names = {"--external-id"},
    hidden = true,
    required = true,
    defaultValue = "--NOT-SET--"
  )
  private String externalId;

  @Option(
    names = {
      "-d",
      "--detach",
    },
    description = "Run Klay in background and print its ID and process identifier (pid)",
    required = true,
    defaultValue = "false"
  )
  private boolean detach;

  public static void main(String[] args) {

    final var command = new CommandLine(Klay.class);
    command.setUnmatchedArgumentsAllowed(true);
    command.setExecutionStrategy(new CommandLine.RunAll());

    System.exit( command.execute(args) );
  }

  @Override
  public void run() {

  }

  public String getExternalId() {
    return this.externalId;
  }

}
