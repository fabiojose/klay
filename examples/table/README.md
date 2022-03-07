# table

Example with KTable, repartioning and materialized state store

Topics:

- free-terminals: terminals available to usage
- busy-terminals: terminals in use

Tables:

-

Run:

```bash
./run.sh
```

Create the topics:

```bash
# free-terminals with 3 partitions
kafka-topics.sh --bootstrap-server localhost:9092 \
--create \
--partitions 3 \
--topic 'free-terminals'

# busy-terminals with 3 partitions
kafka-topics.sh --bootstrap-server localhost:9092 \
--create \
--partitions 3 \
--topic 'busy-terminals'
```

Produce some `free-terminals` events:

```bash
# key..:good
# value:terminal number
kafka-console-producer.sh \
--broker-list localhost:9092 \
--topic 'free-terminals' \
--property 'parse.key=true' \
--property 'key.separator=|' <<EOF
orange|{"number": "1244"}
orange|{"number": "3400"}
lemon|{"number": "1244"}
lemon|{"number": "5612"}
lemon|{"number": "1103"}
orange|{"number": "1500"}
pear|{"number": "1100"}
pear|{"number": "1244"}
EOF
```

Produce some `busy-terminals` events:

```bash
# key..: terminal number
# value: good
kafka-console-producer.sh \
--broker-list localhost:9092 \
--topic 'busy-terminals' \
--property 'parse.key=true' \
--property 'key.separator=|' <<EOF
5601|{"good": "orange"}
5602|{"good": "orange"}
5603|{"good": "pear"}
5604|{"good": "pear"}
EOF
```


Start a consumer to see the results:

```bash
kafka-console-consumer.sh \
--bootstrap-server localhost:9092 \
--from-beginning  \
--topic 'free-terminals' \
--property print.offset=true \
--property print.headers=true \
--property print.key=true \
--property print.partition=true
```
