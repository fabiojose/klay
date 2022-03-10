package com.github.fabiojose.klay.streams;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.github.fabiojose.klay.util.FileWatcher;
import com.github.fabiojose.klay.util.FileWatcher.FileWatchEvent;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TopologyBuilder {

  private static final Logger log = Logger.getLogger(TopologyBuilder.class);

  private static final String NO_VALUE = "##__NO-VALUE__##";
  private static final String STREAM_PROPERTY = "stream";
  private static final String BUILDER_PROPERTY = "builder";
  private static final String PROPERTY_PREFIX = "kafka-streams.";

  @ConfigProperty(name = "klay.stream.from.topic")
  String from;

  @ConfigProperty(name = "klay.stream.to.topic", defaultValue = NO_VALUE)
  String to;

  @ConfigProperty(name = "klay.stream.script", defaultValue = NO_VALUE)
  File file;

  @ConfigProperty(name = "klay.stream.live", defaultValue = "false")
  Boolean liveReload;
  private FileWatcher liveWatcher;

  @Inject
  ManagedExecutor executor;

  private KafkaStreams streams;

  @SuppressWarnings("rawtypes")
  private Optional<KStream> executeGroovyScript(
    final StreamsBuilder builder,
    final KStream<?, ?> stream,
    final File file
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
    binding.setProperty(STREAM_PROPERTY, stream);

    final var groovy = new GroovyShell(binding, config);

    try {
      final var script = groovy.parse(file);
      final var result = script.run();

      if (!NO_VALUE.equals(to)) {
        if (result instanceof KStream) {
          return Optional.of((KStream) result);
        } else {
          log.warnf("The groovy script return is invalid: {}", result);
          throw new IllegalStateException(
            "Your script must return an instance of KStream, not " + result
          );
        }
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    return Optional.empty();
  }

  private Topology build() {
    final var builder = new StreamsBuilder();
    final var stream = builder.stream(from);

    final var resultStream = executeGroovyScript(builder, stream, file);

    resultStream.ifPresent(ks -> ks.to(to));

    return builder.build();
  }

  private void configureLiveReload() {

    if (liveReload && null== liveWatcher) {

      liveWatcher = new FileWatcher(file, this::reStart);
      executor.execute(liveWatcher);

    } else if(!liveReload) {
      log.info("Live reloading disabled.");
    } else if(null!= liveWatcher){
      log.info("Live reloading already started.");
    }

  }

  void reStart(FileWatchEvent event) {
    log.infof("Restarting the kafka streams triggered by event %s", event);
    onStop(null);
    onStart(null);
  }

  void onStart(@Observes StartupEvent evt) {
    log.info("Starting the kafka streams topology.");

    // build streams properties
    final var props = new Properties();
    props.putAll(
      StreamSupport
        .stream(
          ConfigProvider.getConfig().getPropertyNames().spliterator(),
          false
        )
        .filter(propertyName -> propertyName.startsWith(PROPERTY_PREFIX))
        .map(
          propertyName ->
            Map.entry(
              propertyName.substring(PROPERTY_PREFIX.length()),
              ConfigProvider.getConfig().getConfigValue(propertyName).getValue()
            )
        )
        .peek(System.out::println)
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue))
    );

    streams = new KafkaStreams(build(), props);
    streams.start();

    configureLiveReload();
  }

  void onStop(@Observes ShutdownEvent evt) {
    streams.close();
    streams.cleanUp();
    log.info("Kafka streams topology closed.");
  }

  public KafkaStreams getStreams() {
    return streams;
  }
}
