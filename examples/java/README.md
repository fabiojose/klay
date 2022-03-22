# join written using Java

Example with (inner) join of two source streams and sinking to `paid` topic.

Tools:

- Klay
- Kafka CLI

Topics:

- orders
- payments
- paid

Start the example:

```console
./start.sh

# CTRL+C to stop
```

Produce some `orders` events:

> TIP: use the command `klay ps` to see the value of `<BROKER_PORT>`

```console
kafka-console-producer.sh \
--broker-list localhost:<BROKER-PORT> \
--topic orders \
--property "parse.key=true" \
--property "key.separator=|" <<EOF
2002|{"id": "2002", "value": 44.3}
2003|{"id": "2003", "value": 2.99}
2004|{"id": "2004", "value": 23.0}
2005|{"id": "2005", "value": 199.99}
EOF
```

Produce some `payments` events:

```console
kafka-console-producer.sh \
--broker-list localhost:<BROKER-PORT> \
--topic payments \
--property "parse.key=true" \
--property "key.separator=|" <<EOF
2002|{"code": "y300"}
2003|{"code": "t900"}
2004|{"code": "r500"}
2005|{"code": "p560"}
EOF
```

Start a consumer on `paid` topic to see the results:

```console
kafka-console-consumer.sh \
--bootstrap-server localhost:<BROKER-PORT> \
--from-beginning  \
--topic paid \
--property print.partition=true \
--property print.offset=true \
--property print.headers=true \
--property print.key=true
```
