# 🕹️ Klay

Play with Apache Kafka®. Good for proof-of-concept, labs, fast development and study.

- run Kafka Streams with steps written in Groovy or Java
  - 😃 with live (hot) reloading
- Rest API to interact with state stores and streams
- start an Apache Kafka® embedded

__Features__

- [x] Start Apache Kafka® Server
- [x] Start Kafka Streams written in Groovy
- [x] Start Kafka Streams written in Java
- [x] Rest API for Kafka Streams State Stores
- [ ] Wrappers
  - [x] kafka-topics
  - [ ] kafka-console-consumer
  - [ ] kafka-console-producer

## Installation

To install or update Klay.

```console
bash -c "$(curl -fsSL https://raw.githubusercontent.com/fabiojose/klay/main/install.sh)"
```

If you get the following message, everying is ok:

```console
🕹️ Klay version <version> installed and configured with success.
   ✅ Installation
   ✅ Configuration
```

Open another terminal and type:

```console
klay --help
```

_Manual Installation_

To perform the installation manually, follow these steps.

TODO:

## Kafka Streams

Run Kafka Streams applications with the easiness and flexibility of scripting.

Default imports:

- `org.apache.kafka.streams.*`
- `org.apache.kafka.streams.kstream.*`
- `org.apache.kafka.streams.state.*`

> But you can import any class available in the classpath

Available objects:

- `builder`
- `stream`

Default Serdes:

- key: `String`
- value: `JsonSerde`

### Groovy

The script must return an instance of KStream. Example:

```groovy
// TODO:

```

### Java

```java
// TODO:

```

## Rest API

TODO

## Starting the Apache Kafka®

TODO

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./gradlew build
```
It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Thanks

- Klay icon: <a href="https://www.flaticon.com/free-icons/gamepad" title="gamepad icons">Gamepad icons created by Vitaly Gorbachev - Flaticon</a>
