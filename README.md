# 🕹️ Klay

Play with Apache Kafka®. Good for proof-of-concept, labs, fast development and study.

- run Kafka Streams with steps written in Groovy or Java
  - 😃 with live (hot) reloading
- Rest API to interact with state stores and streams
- start an Apache Kafka® embedded

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

## Creating a native executable

You can create a native executable using:
```shell script
./gradlew build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:
```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/runstreams-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling.
