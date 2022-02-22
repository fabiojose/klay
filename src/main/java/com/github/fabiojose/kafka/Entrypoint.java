package com.github.fabiojose.kafka;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Entrypoint {

  public static void main(String[] args) {
    Quarkus.run(args);
  }

}
