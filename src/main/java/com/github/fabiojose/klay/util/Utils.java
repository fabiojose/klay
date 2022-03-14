package com.github.fabiojose.klay.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public final class Utils {

  private Utils() {}

  public static int freeTCPPort() {
    try (ServerSocket socket = new ServerSocket(0)) {
      socket.setReuseAddress(true);
      return socket.getLocalPort();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static Properties propertiesOf(String path) {
    final var properties = new Properties();
    try (var inStream = Utils.class.getResourceAsStream(path)) {
      properties.load(inStream);
      return properties;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static void write(String line, Path outFile) {
    try {
      Files.writeString(
        outFile,
        line,
        StandardOpenOption.WRITE,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND,
        StandardOpenOption.DSYNC
      );
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static void write(long value, Path outFile) {
    write(String.valueOf(value), outFile);
  }
}
