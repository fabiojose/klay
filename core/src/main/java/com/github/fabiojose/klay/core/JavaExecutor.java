package com.github.fabiojose.klay.core;

import com.github.fabiojose.klay.util.Compiler;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;

/**
 * Executes the Java source to build the Kafka Streams Topology.
 */
public class JavaExecutor {

  private Compiler javaCompiler;

  private Compiler getJavaCompiler() {
    if (null == javaCompiler) {
      this.javaCompiler = Compiler.getInstance();
    }

    return this.javaCompiler;
  }

  /**
   * @param fromStream Source stream, that should be {@code null}
   * @param builder Kafka Streams builder
   * @param source Groovy source string
   * @return {@link Optional#empty()} when there is no Sink stream
   */
  public Optional<KStream> execute(
    KStream fromStream,
    StreamsBuilder builder,
    String source,
    String sinkTopic
  ) {
    var topology = getJavaCompiler().compileAndCreateInstance(source);

    var result = topology.build(fromStream, builder);
    if (null != sinkTopic) {
      if (null != result) {
        return Optional.of(result);
      } else {
        throw new IllegalStateException(
          "Your script must return an instance of KStream"
        );
      }
    }

    return Optional.empty();
  }


  /**
   * @param fromStream Source stream, that should be {@code null}
   * @param builder Kafka Streams builder
   * @param source Groovy source file
   * @return {@link Optional#empty()} when there is no Sink stream
   */
  public Optional<KStream> execute(
    KStream fromStream,
    StreamsBuilder builder,
    Path source,
    String sinkTopic
  ) {

    try {
      var sourceString = Files.readString(source);
      return execute(fromStream, builder, sourceString, sinkTopic);
    }catch(IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
