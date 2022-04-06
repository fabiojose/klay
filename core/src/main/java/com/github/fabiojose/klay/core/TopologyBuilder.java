package com.github.fabiojose.klay.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Optional;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

/**
 * Supplies the topology built by java or groovy sources.
 */
public class TopologyBuilder {

  private Optional<String> from;
  private Optional<String> to;

  private GroovyExecutor groovy;
  private JavaExecutor java;

  public TopologyBuilder() {
    this.from = Optional.empty();
    this.to = Optional.empty();
  }

  public TopologyBuilder(String from, String to) {
    this.from = Optional.ofNullable(from);
    this.to = Optional.ofNullable(to);
  }

  private GroovyExecutor getGroovyExecutor() {
    if (null == this.groovy) {
      this.groovy = new GroovyExecutor();
    }

    return this.groovy;
  }

  private JavaExecutor getJavaExecutor() {
    if (null == this.java) {
      this.java = new JavaExecutor();
    }

    return this.java;
  }

  @SuppressWarnings("rawtypes")
  private Optional<KStream> buildTopology(
    final KStream fromStream,
    StreamsBuilder builder,
    Path source
  ) {
    final var fileName = source.getFileName().toString().toLowerCase();
    if (fileName.endsWith(".groovy")) {
      return getGroovyExecutor()
        .execute(fromStream, builder, source, this.to.orElseGet(() -> null));
    } else if (fileName.endsWith(".java")) {
      return getJavaExecutor()
        .execute(fromStream, builder, source, this.to.orElseGet(() -> null));
    } else {
      throw new IllegalArgumentException("Unsupported file type: " + fileName);
    }
  }

  private Optional<KStream> buildTopology(
    final KStream fromStream,
    final StreamsBuilder builder,
    final String source,
    final StreamSourceType sourceType
  ) {
    if (StreamSourceType.GROOVY.equals(sourceType)) {
      return getGroovyExecutor()
        .execute(fromStream, builder, source, this.to.orElseGet(() -> null));
    } else if (StreamSourceType.JAVA.equals(sourceType)) {
      return getJavaExecutor()
        .execute(fromStream, builder, source, this.to.orElseGet(() -> null));
    } else {
      throw new IllegalArgumentException("sourceType: " + sourceType);
    }

  }

  public Topology topologyOf(Path source) {
    final var builder = new StreamsBuilder();

    KStream fromStream = from.map(builder::stream).orElseGet(() -> null);

    final var resultStream = buildTopology(fromStream, builder, source);

    resultStream.ifPresent(
      sink -> {
        to.ifPresent(sink::to);
      }
    );

    return builder.build();
  }

  public Topology topologyOf(
    final InputStream source,
    StreamSourceType sourceType
  ) {
    try {
      return topologyOf(new String(source.readAllBytes(), "UTF-8"), sourceType);
    }catch(IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public Topology topologyOf(final String source, StreamSourceType sourceType) {
    final var builder = new StreamsBuilder();
    final KStream fromStream = from.map(builder::stream).orElseGet(() -> null);

    final var resultStream = buildTopology(
      fromStream,
      builder,
      source,
      sourceType
    );

    resultStream.ifPresent(
      sink -> {
        to.ifPresent(sink::to);
      }
    );

    return builder.build();
  }
}
