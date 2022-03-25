package com.github.fabiojose.klay.util;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

public class MetadataWriter {

  private final String externalId;

  private MetadataWriter(String externalId) {
    this.externalId = Objects.requireNonNull(externalId);
  }

  public static MetadataWriter of(String externalId) {
    return new MetadataWriter(externalId);
  }

  private Map<String, Path> files = new HashMap<>();

  private Path fileOf(String name) {
    var file = files.get(name);
    if (null == file) {
      file =
        Path.of(
          Optional
            .ofNullable(System.getenv("KLAY_HOME"))
            .orElseThrow(
              () ->
                new NoSuchElementException(
                  "KLAY_HOME not found in your environment"
                )
            ),
          "processes.d",
          externalId,
          name
        );

      files.put(name, file);
    }

    return file;
  }

  public void pid(long pid) {

    var pidFile = fileOf("pid");
    Utils.write(pid, pidFile);

  }

  public void type(String type) {

    var typeFile = fileOf("type");
    Utils.write(type, typeFile);

  }

  public void ports(String... ports) {

    var portsFile = fileOf("ports");
    for(String port : ports) {
      Utils.write(port, portsFile);
    }

  }

  public void version(String version) {

    var versionFile = fileOf("version");
    Utils.write(version, versionFile);

  }
}
