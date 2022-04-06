package com.github.fabiojose.klay.util;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.joor.Reflect;

/**
 * Java Compiler utility.
 */
public final class Compiler {

  private static final Logger log = LoggerFactory.getLogger(Compiler.class);

  private final String klayUberJarLocation;
  private Compiler() {
    this.klayUberJarLocation = System.getenv("KLAY_UBER_JAR_LOCATION");
    if(null== this.klayUberJarLocation){
      throw new IllegalStateException("environment variable not set: KLAY_UBER_JAR_LOCATION");
    }
  }

  public static Compiler getInstance() {
    return new Compiler();
  }

  public K compileAndCreateInstance(final String source) {

    try {
      log.debug("original java source {}", source);

      var modifiedSource = "import " + K.class.getCanonicalName() + ";" + source;
      log.debug("source to be compiled {}", modifiedSource);

      return Reflect.compile("t", modifiedSource).create().get();

    }catch(NullPointerException e) {
      log.error(e.getLocalizedMessage(), e);
      if(e.getMessage().endsWith("\"org.joor.Reflect.type()\" is null")){
        // infer wrong class name
        throw new CompilerException("Wrong class name, must be t");
      } else {
        throw e;
      }
    }
  }

  public K compileAndCreateInstance(final File source) {

    try {
      final var sourceString = Files.readString(source.toPath());
      return compileAndCreateInstance(sourceString);
    }catch(IOException e) {
      throw new UncheckedIOException(e);
    }

  }

  public static class CompilerException extends RuntimeException {
    public CompilerException(String message){
      super(message);
    }
  }
}
