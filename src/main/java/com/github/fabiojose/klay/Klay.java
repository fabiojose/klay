package com.github.fabiojose.klay;

import com.github.fabiojose.klay.util.HoldCommands;
import com.github.fabiojose.klay.util.MetadataWriter;

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
    HoldCommands.LogsCommand.class,
    HoldCommands.DescribeCommand.class
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

    // Add programmatic created commands
    command.addSubcommand(KafkaCLICommand.programmatic());

    command.setUnmatchedArgumentsAllowed(true)
      .setExecutionStrategy(new CommandLine.RunAll());

    System.exit( command.execute(args) );
  }

  private void writeMetadata() {

    var metadata = MetadataWriter.of(getExternalId());

    //pid
    metadata.pid(ProcessHandle.current().pid());

  }

  @Override
  public void run() {
    writeMetadata();
  }

  public String getExternalId() {
    return this.externalId;
  }

}
