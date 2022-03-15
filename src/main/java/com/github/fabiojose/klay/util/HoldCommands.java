package com.github.fabiojose.klay.util;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

public class HoldCommands {

  @Command(
    name = "ps",
    description = "List processes",
    mixinStandardHelpOptions = true
  )
  public static class PsCommand {

    @Option(
      names = {"-a", "--all"},
      description = "Show all processes (default shows just running)"
    )
    boolean all;

  }

  @Command(
    name = "stop",
    description = "Stop one or more running processes",
    mixinStandardHelpOptions = true
  )
  public static class StopCommand {

    @Parameters(
      paramLabel = "KLAY-ID",
      description = "Given id of running process",
      arity = "1..*"
    )
    String[] klayIds;
  }
}
