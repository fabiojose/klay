package com.github.fabiojose.klay.broker;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.admin.AdminServer.AdminServerException;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.jboss.logging.Logger;

public class StartZookeeper {

  private static final Logger log = Logger.getLogger(StartZookeeper.class);

  private final Properties properties;
  private final CountDownLatch latch;

  private Thread runner;

  StartZookeeper(Properties properties, CountDownLatch latch) {
    this.properties = Objects.requireNonNull(properties);
    this.latch = Objects.requireNonNull(latch);
  }

  void start() {
    log.debugf("Zookeeper modified properties %s", properties);

    final var config = new QuorumPeerConfig();
    try {
      config.parseProperties(properties);

      final var cfg = new ServerConfig();
      cfg.readFrom(config);
      log.infof("Zookeeper properties %s", properties);

      final var zookeeper = new ZooKeeperServerMain();

      //TODO: Some listener to watch server status and decrease the latch

      this.runner =
        new Thread(
          () -> {
            try {
              zookeeper.runFromConfig(cfg);
            } catch (IOException | AdminServerException e) {
              log.error(e.getMessage(), e);
            }
          }
        );
      runner.setDaemon(true);
      runner.start();
      log.info("Starting zookeeper ensemble.");

    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (ConfigException e) {
      throw new IllegalStateException(e);
    }
  }

  public void join() throws InterruptedException {
    runner.join();
  }
}