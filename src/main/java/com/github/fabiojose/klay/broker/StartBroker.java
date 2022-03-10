package com.github.fabiojose.klay.broker;

import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;
import org.apache.kafka.common.utils.SystemTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;

public class StartBroker {

  static final String PORT_PROPERTY = "port";

  private static final Logger log = LoggerFactory.getLogger(
    StartBroker.class
  );

  private final Properties properties;
  private KafkaServer server;

  /**
   * @param properties Default Apache Kafka® properties.
   * @param overrides Properties to overrides the default ones.
   */
  StartBroker(
    final Properties properties
  ) {
    this.properties = Objects.requireNonNull(properties);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  void start() {
    var configurations = new HashMap();
    configurations.putAll(properties);
    log.debug("Apache Kafka® server properties {}", configurations);

    var config = new KafkaConfig(configurations);
    server = new KafkaServer(
      config,
      new SystemTime(),
      Option.apply(StartBroker.class.getSimpleName()),
      false
    );

    server.startup();
    server.awaitShutdown();

    log.info("Apache Kafka® server started.");
  }

  void shutdown() {
    server.shutdown();
  }
}
