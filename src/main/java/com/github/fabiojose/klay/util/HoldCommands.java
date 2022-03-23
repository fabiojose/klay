package com.github.fabiojose.klay.util;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Help.Visibility;

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

  @Command(
    name = "logs",
    description = "Fetch the logs of a process",
    mixinStandardHelpOptions = true
  )
  public static class LogsCommand {

    @Option(
      names = {"-f", "--follow"},
      description = "Follow log output"
    )
    boolean follow;

    @Parameters(
      paramLabel = "KLAY-ID",
      description = "Given id of process",
      arity = "1"
    )
    String klayId;
  }

  @Command(
    name = "describe",
    description = "Show details of a specific process",
    mixinStandardHelpOptions = true
  )
  public static class DescribeCommand {

    @Parameters(
      paramLabel = "KLAY-ID",
      description = "Given id of process",
      arity = "1"
    )
    String klayId;

    @Option(
      names = {"-o", "--output"},
      paramLabel = "FORMAT",
      description = "Output format. Valid values: ${COMPLETION-CANDIDATES}",
      defaultValue = "PLAIN",
      required = true,
      showDefaultValue = Visibility.ALWAYS
    )
    OutputType format;
  }

  public static enum OutputType {
    PLAIN,
    CSV;
  }

  @Command(
    name = "prune",
    description = "Remove unused data of all stopped processes",
    mixinStandardHelpOptions = true
  )
  public static class PruneCommand {

  }
}
