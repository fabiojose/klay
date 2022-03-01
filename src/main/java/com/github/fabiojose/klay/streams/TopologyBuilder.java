package com.github.fabiojose.klay.streams;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Slf4j
@ApplicationScoped
public class TopologyBuilder {

  private static final String NO_VALUE = "##__NO-VALUE__##";
  private static final String STREAM_PROPERTY = "stream";
  private static final String BUILDER_PROPERTY = "builder";

  @ConfigProperty(name = "klay.stream.from.topic")
  private String from;

  @ConfigProperty(name = "klay.stream.to.topic", defaultValue = NO_VALUE)
  private String to;

  @ConfigProperty(name = "klay.stream.script", defaultValue = NO_VALUE)
  private File file;

  @SuppressWarnings("rawtypes")
  private KStream executeGroovyScript(
    final StreamsBuilder builder,
    final KStream<?, ?> stream,
    final File file
  ) {
    final var imports = new ImportCustomizer();
    imports.addStarImports(
      "org.apache.kafka.streams",
      "org.apache.kafka.streams.kstream"
    );

    final var config = new CompilerConfiguration();
    config.addCompilationCustomizers(imports);

    final var binding = new Binding();
    binding.setProperty(BUILDER_PROPERTY, builder);
    binding.setProperty(STREAM_PROPERTY, stream);

    final var groovy = new GroovyShell(binding, config);

    try {
      final var script = groovy.parse(file);
      final var result = script.run();

      if (!NO_VALUE.equals(to) && result instanceof KStream) {
        return (KStream) result;
      } else {
        log.warn("The groovy script return is invalid: {}", result);
        throw new IllegalStateException(
          "Your script must return an instance of KStream, not " + result
        );
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Produces
  public Topology build() {
    final var builder = new StreamsBuilder();
    final var stream = builder.stream(from);

    final var resultStream = executeGroovyScript(builder, stream, file);

    if (!NO_VALUE.equals(to)) {
      resultStream.to(to);
    } else {
      log.info("Stream has no sink topic");
    }

    return builder.build();
  }
}
