package com.github.fabiojose.klay.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.jboss.logging.Logger;

public class FileWatcher implements Runnable {

  private static final Logger log = Logger.getLogger(FileWatcher.class);

  private final File fileToWatch;
  private final Consumer<FileWatchEvent> callback;

  public FileWatcher(final File fileToWatch, Consumer<FileWatchEvent> callback) {
    this.fileToWatch = Objects.requireNonNull(fileToWatch);
    this.callback = Objects.requireNonNull(callback);
  }

  private WatchService watcher;

  private void init() throws IOException {
    if (null == watcher) {
      watcher = FileSystems.getDefault().newWatchService();

      final var path = Path.of(fileToWatch.getParent());
      path.register(
        watcher,
        StandardWatchEventKinds.ENTRY_MODIFY
      );
      log.infof("Live reload started for %s", fileToWatch);
    }
  }

  private void processEvent(List<WatchEvent<?>> events) {
    var changed = events
      .stream()
      .filter(event -> (event.context() instanceof Path))
      .filter(event -> ((Path)event.context()).endsWith(fileToWatch.getName()))
      .findFirst();

    changed.ifPresent(
      event -> {
        log.infof("File changed %s, reload started", event.context());
        callback.accept(new FileWatchEvent((Path)event.context(), event));
      }
    );
  }

  @Override
  public void run() {
    try {
      init();
      WatchKey watch;
      while ((watch = watcher.take()) != null) {
        processEvent(watch.pollEvents());
        watch.reset();
      }
      log.infof("No more changes to watch for file %s", fileToWatch);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    } catch (InterruptedException e) {
      log.error(e.getMessage(), e);
    }
  }

  @SuppressWarnings("rawtypes")
  public static record FileWatchEvent(Path file, WatchEvent parent) {
  }
}
