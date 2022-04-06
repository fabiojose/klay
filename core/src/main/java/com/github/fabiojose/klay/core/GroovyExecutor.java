package com.github.fabiojose.klay.core;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executes the Groovy source to build the Kafka Streams Topology.
 */
public class GroovyExecutor {

  private static final Logger log = LoggerFactory.getLogger(
    GroovyExecutor.class
  );

  static final String STREAM_PROPERTY = "stream";
  static final String BUILDER_PROPERTY = "builder";

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
    final var imports = new ImportCustomizer();
    imports.addStarImports(
      "org.apache.kafka.streams",
      "org.apache.kafka.streams.kstream",
      "org.apache.kafka.streams.state"
    );

    final var config = new CompilerConfiguration();
    config.addCompilationCustomizers(imports);

    final var binding = new Binding();
    binding.setProperty(BUILDER_PROPERTY, builder);
    binding.setProperty(STREAM_PROPERTY, fromStream);

    final var groovy = new GroovyShell(binding, config);

    final var script = groovy.parse(source);
    final var result = script.run();

    if (null != sinkTopic) {
      if (result instanceof KStream) {
        return Optional.of((KStream) result);
      } else {
        log.warn("The groovy script return is invalid: {}", result);
        throw new IllegalStateException(
          "Your script must return an instance of KStream, not " + result
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
      final var sourceString = Files.readString(source);

      return execute(fromStream, builder, sourceString, sinkTopic);

    } catch(IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
