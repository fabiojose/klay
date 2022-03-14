package com.github.fabiojose.klay.broker;

import com.github.fabiojose.klay.util.Utils;
import java.io.File;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.streams.state.HostInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * To start the zookeeper and the broker transparently
 */
public class StartApacheKafka {

  private static final Logger log = LoggerFactory.getLogger(
    StartApacheKafka.class
  );

  private static final String ZOOKEEPER_CONNECT_PROPERTY = "zookeeper.connect";

  private int zookeeperPort;
  private Optional<HostInfo> zookeeperBinding;
  private Optional<StartZookeeper> zookeeper = Optional.empty();

  private int brokerPort;
  private StartBroker broker;

  private final boolean startZookeeper;
  private final Properties zookeeperOverrideProperties;
  private final Properties brokerOverrideProperties;

  StartApacheKafka(
    boolean startZookeeper,
    Properties zookeeperOverrideProperties,
    Properties brokerOverrideProperties
  ) {
    this.startZookeeper = startZookeeper;
    this.zookeeperOverrideProperties =
      Objects.requireNonNull(zookeeperOverrideProperties);
    this.brokerOverrideProperties =
      Objects.requireNonNull(brokerOverrideProperties);
  }

  private void startZookeeperIfTrue(final String prefix) {
    if (this.startZookeeper) {
      final var defaultProperties = Utils.propertiesOf("/zookeeper.properties");
      log.debug("Zookeeper default properties: {}", defaultProperties);

      final var configurations = new Properties();
      configurations.putAll(defaultProperties);
      configurations.putAll(this.zookeeperOverrideProperties);

      this.zookeeperPort = Utils.freeTCPPort();
      configurations.setProperty(
        "clientPort",
        String.valueOf(this.zookeeperPort)
      );
      configurations.setProperty(
        "dataDir",
        new File(System.getProperty("java.io.tmpdir"), prefix + "-zookeeper")
          .getAbsolutePath()
      );
      log.debug("Actual zookeeper properties {}", configurations);

      zookeeper =
        Optional.of(new StartZookeeper(configurations));

      zookeeper.get().start();
      this.zookeeperBinding =
        Optional.of(new HostInfo("localhost", zookeeperPort));
    } else {
      log.info("No zookeeper ensemble will be started.");
      this.zookeeperBinding = Optional.empty();
    }
  }

  void start() {
    final var prefix = UUID.randomUUID().toString();

    // Zookeeper
    startZookeeperIfTrue(prefix);

    // Broker
    final var brokerDefaultProperties = Utils.propertiesOf(
      "/broker.properties"
    );
    log.debug("Broker default properties: {}", brokerDefaultProperties);

    this.brokerPort = Utils.freeTCPPort();
    brokerDefaultProperties.setProperty(
      StartBroker.PORT_PROPERTY,
      String.valueOf(this.brokerPort)
    );

    final var configurations = new Properties();
    configurations.putAll(brokerDefaultProperties);
    configurations.putAll(brokerOverrideProperties);
    this.brokerPort =
      Integer.valueOf(configurations.getProperty(StartBroker.PORT_PROPERTY));

    final var zookeeperConnect =
      this.zookeeperBinding.map(
          binding -> binding.host() + ":" + binding.port()
        )
        .or(
          () ->
            Optional.ofNullable(
              configurations.getProperty(ZOOKEEPER_CONNECT_PROPERTY)
            )
        )
        .orElseThrow(
          () ->
            new NoSuchElementException(
              "Must start the Zookeeper (--zookeeper) or provide the broker property called zookeeper.connect"
            )
        );
    configurations.setProperty(ZOOKEEPER_CONNECT_PROPERTY, zookeeperConnect);

    configurations.setProperty(
      "log.dir",
      new File(System.getProperty("java.io.tmpdir"), prefix + "-kafka-logs")
        .getAbsolutePath()
    );
    log.debug("Actual broker configurations {}", configurations);

    Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

    broker = new StartBroker(configurations);
    broker.start();
  }

  void shutdown() {
    broker.shutdown();
    zookeeper.ifPresent(StartZookeeper::shutdown);
  }
}
