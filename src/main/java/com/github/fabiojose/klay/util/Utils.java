package com.github.fabiojose.klay.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;

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

}
