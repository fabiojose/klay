# join

Example with (inner) join of two source streams and sinking to `paid` topic.

Topics:

- orders
- payments
- paid

Run:

```bash
./run.sh
```

Produce some `orders` events:

```bash
kafka-console-producer.sh \
  --broker-list localhost:9092 \
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

```bash
kafka-console-producer.sh \
  --broker-list localhost:9092 \
  --topic payments \
  --property "parse.key=true" \
  --property "key.separator=|" <<EOF
2002|{"code": "y300"}
2003|{"code": "t900"}
2004|{"code": "r500"}
2005|{"code": "p560"}
EOF
```
