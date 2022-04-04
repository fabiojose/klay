package com.github.fabiojose.klay.util;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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

  public K compileAndCreateInstance(final File sourceFile) {

    try {
      final var originalSource = Files.readString(sourceFile.toPath());
      log.debug("original java source {}", originalSource);

      var source = "import " + K.class.getCanonicalName() + ";" + originalSource;
      log.debug("source to be compiled {}", source);

      return Reflect.compile("t", source).create().get();

    }catch(IOException e) {
      throw new UncheckedIOException(e);
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

  public static class CompilerException extends RuntimeException {
    public CompilerException(String message){
      super(message);
    }
  }
}
