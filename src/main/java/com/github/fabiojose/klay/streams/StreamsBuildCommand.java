package com.github.fabiojose.klay.streams;

import picocli.CommandLine.Command;

@Command(
  name = "streams",
  description = "Build uber-jar with your stream topology ready to execute",
  mixinStandardHelpOptions = true
)
public class StreamsBuildCommand implements Runnable {

  // TODO: Same parameters and options of StreamsCommand ?

  // String packageName;

  @Override
  public void run() {

    // TODO: Split klay: core and cli

    // TODO: Publish to sonatype

    // TODO: Needs gradle 7.4.1: download

    // TODO: Classes of kay to start streams

    // TODO: Dependencies needed

    // TODO: Collect deps
    // quarkus
    // custom QuarkusMain: a java file. Template ?
    // custom TopologyBuilder: a java file. Template ?

    // TODO: Build java files

    // TODO: Build uber-jar
    //  with compiled java files. Template?
    //  with compiled java topology, if java
    //  with groovy file, if groovy

    // TODO: Generate application.properties

    // TODO: Generate Dockerfile. Template?

    // TODO: Generate build-and-run.sh

  }

}
